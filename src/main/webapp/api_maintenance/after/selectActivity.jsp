<%@ page import="java.util.List" %>
<%@ page import="com.tw.api_maintenance.after.TeamBuildingPackageItemDto" %>
<%@ page import="com.tw.api_maintenance.after.ActivityItemDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    TeamBuildingPackageItemDto teamBuildingPackageItem = (TeamBuildingPackageItemDto) request.getAttribute("teamBuildingPackageItem");
    List<ActivityItemDto> activityItems = teamBuildingPackageItem.getActivityItemDtos();
%>

<html>
<head>
    <title>团建活动选择（API治理）</title>
    <script type="text/javascript" src="/api_maintenance/after/config.js"></script>
    <script type="text/javascript" src="/api_maintenance/after/activityInfoRow.js"></script>
    <script type="text/javascript" src="/api_maintenance/public/jquery-3.6.0.js"></script>
</head>
<body>
<h1 id="product_item_<%=teamBuildingPackageItem.getId()%>"><%=teamBuildingPackageItem.getName()%>
</h1>
<h2>团建活动列表</h2>
<table border="1px">
    <tr>
        <th>选择</th>
        <th>活动</th>
        <th>参加人数</th>
    </tr>
    <%for (int i = 0; i < activityItems.size(); i++) {%>
    <tr>
        <td><input id="activity_item_<%=activityItems.get(i).getId()%>"
                   type="checkbox"
            <%if(activityItems.get(i).getSelected()) {%>
                   checked
            <%}%>
                   onclick="clickActivityCheck(<%=teamBuildingPackageItem.getId()%>, <%=activityItems.get(i).getId()%>)">
        </td>
        <td><span><%=activityItems.get(i).getName()%></span></td>
        <td><input id="activity_item_<%=activityItems.get(i).getId()%>_count"
                   value="<%=activityItems.get(i).getCount() == null ? new String() : activityItems.get(i).getCount()%>">
        </td>
    </tr>
    <%}%>
</table>

<h2>业务描述：</h2>
<p>团队不定期进行团建。公司有不同的团建礼包，礼包包含多个活动。</p>
<ul>
    <li>
        <h3>选择活动：</h3>
        <ul>
            <li>参加人数不能为空，并且为1-50（包括50）之间的整数</li>
            <li>某些活动不能同时勾选，比如旅游类活动在"十万用户团建礼包"中只能选择一个</li>
            <li>某些活动之间有依赖，比如只有选择"两日游"活动才可以选择"住宿"，但"两日游"也可以不选择"住宿"</li>
            <li>某些活动上个月选择过则本次不能选择，比如上次选择了"唱歌"，本月不能选择；"吃饭"则可以每月选择</li>
            <li>发送请求，记录活动选项</li>
        </ul>
    </li>
    <li>
        <h3>取消选择活动：</h3>
        <ul>
            <li>清空参加人数</li>
            <li>某些活动之间有依赖，比如取消了"两日游"项目，则一定要取消"住宿"</li>
            <li>发送请求，记录活动选项</li>
        </ul>
    </li>
</ul>

<h2>当前实现存在的问题：</h2>
<ul style="color: red;">
    <li>部分数据校验只存在于jsp</li>
    <li>一个原子业务通过多个API调用完成（校验和保存数据应该在一个API，取消依赖活动应该也在同一个API）</li>
</ul>

<script>
    var activityItemIdToActivityId = {};
    <%for(int i = 0; i < activityItems.size(); i ++) {%>
    activityItemIdToActivityId[<%=activityItems.get(i).getId()%>] = <%=activityItems.get(i).getActivityId()%>;
    <%}%>

    var activityIdToActivityItemId = {};
    <%for(int i = 0; i < activityItems.size(); i ++) {%>
    activityIdToActivityItemId[<%=activityItems.get(i).getActivityId()%>] = <%=activityItems.get(i).getId()%>;
    <%}%>

    function getTeamBuilderPackageId() {
        return <%=teamBuildingPackageItem.getPackageId()%>;
    }

    function validateMutexActivities(packageId, activityId) {
        var mutexActivityId = mutexActivityConfig[packageId] && mutexActivityConfig[packageId][activityId];
        var mutexActivityItemId = activityIdToActivityItemId[mutexActivityId];
        if (mutexActivityItemId) {
            let activityInfoRow = new ActivityInfoRow(mutexActivityItemId);
            if (activityInfoRow.isChecked()) {
                return {
                    success: false,
                    errorMessage: "在" + packageConfig[packageId] + "中，" + activityConfig[activityId] + "和" + activityConfig[mutexActivityId] + "不能同时选择！"
                }
            }
        }

        return {success: true};
    }

    function validateCount(activityInfoRow) {
        if (!activityInfoRow.isCountBetween(1, 50)) {
            return {
                success: false,
                errorMessage: "参加人数必须在1到50之间！"
            };
        }

        return {success: true}
    }

    function validateReliedActivities(packageId, activityId) {
        var reliedActivityId = reliedActivityConfig[packageId] && reliedActivityConfig[packageId][activityId];
        let reliedActivityItemId = activityIdToActivityItemId[reliedActivityId];

        if (reliedActivityItemId) {
            let activityInfoRow = new ActivityInfoRow(reliedActivityItemId);
            if (!activityInfoRow.isChecked()) {
                return {
                    success: false,
                    errorMessage: "在" + packageConfig[packageId] + "中，选择" + activityConfig[activityId] + "前必须选择" + activityConfig[reliedActivityId] + "！"
                }
            }
        }

        return {success: true};
    }

    function validateActivities(activityInfoRow, packageItemId, activityItemId) {
        var result = validateCount(activityInfoRow);
        if (result.success) {
            var packageId = getTeamBuilderPackageId();
            var activityId = activityItemIdToActivityId[activityItemId];
            result = validateMutexActivities(packageId, activityId);
            if (result.success) {
                result = validateReliedActivities(packageId, activityId);
            }
        }
        return result;
    }

    function selectActivity(activityInfoRow, packageItemId, activityItemId) {
        var result = validateActivities(activityInfoRow, packageItemId, activityItemId);
        if (result.success) {
            result = checkMutexActivity(packageItemId, activityItemId)
            if (result.success) {
                result = createActivity(activityInfoRow, packageItemId, activityItemId);
            }
        }
        return result;
    }

    function unSelectRelyer(packageItemId, activityItemId) {
        var packageId = getTeamBuilderPackageId();
        var activityId = activityItemIdToActivityId[activityItemId];
        var relyer = relierActivityConfig[packageId] && relierActivityConfig[packageId][activityId];
        var relyerItemId = activityIdToActivityItemId[relyer];
        if (relyerItemId) {
            var activityInfoRow = new ActivityInfoRow(relyerItemId);
            if (activityInfoRow.isChecked()) {
                unSelectActivity(activityInfoRow, packageItemId, relyerItemId);
            }
        }
    }

    function unSelectSingleActivity(activityInfoRow, packageItemId, activityItemId) {
        activityInfoRow.setChecked(false);
        activityInfoRow.clearCount();
        cancelActivity(packageItemId, activityItemId);
    }

    function unSelectActivity(activityInfoRow, packageItemId, activityItemId) {
        unSelectRelyer(packageItemId, activityItemId);
        unSelectSingleActivity(activityInfoRow, packageItemId, activityItemId);
    }

    function clickActivityCheck(packageItemId, activityItemId) {
        let activityInfoRow = new ActivityInfoRow(activityItemId);
        if (activityInfoRow.isChecked()) {
            var result = selectActivity(activityInfoRow, packageItemId, activityItemId);
            if (!result.success) {
                alert(result.errorMessage);
                activityInfoRow.setChecked(false);
            }
        } else {
            unSelectActivity(activityInfoRow, packageItemId, activityItemId);
        }
    }

    function checkMutexActivity(packageItemId, activityItemId) {
        var result = {success: true};
        let requestBody = {
            teamBuildingPackageItemId: packageItemId,
            activityItemId: activityItemId
        };

        $.ajax({
            type: "POST",
            url: "/api-maintenance/after?sAction=check-mutex",
            data: requestBody,
            async: false,
            success: function () {
                result.success = true;
                console.log('发送"校验互斥"请求结果成功:' + JSON.stringify(requestBody));
            },
            error: function (jqXHR) {
                result.success = false;
                result.errorMessage = generateErrorMessage(jqXHR.responseJSON);
                console.log('发送"选择活动"请求结果失败:' + JSON.stringify(requestBody));
            }
        });
        return result;
    }

    function createActivity(activityInfoRow, packageItemId, activityItemId) {
        var result = {};
        let requestBody = {
            teamBuildingPackageItemId: packageItemId,
            activityItemId: activityItemId,
            count: activityInfoRow.getCount()
        };

        $.ajax({
            type: "POST",
            url: "/api-maintenance/after?sAction=select",
            data: requestBody,
            async: false,
            success: function () {
                result.success = true;
                console.log('发送"选择活动"请求成功:' + JSON.stringify(requestBody));
            },
            error: function (jqXHR) {
                result.success = false;
                result.errorMessage = generateErrorMessage(jqXHR.responseJSON)
                console.log('发送"选择活动"请求失败:' + JSON.stringify(requestBody));
            }
        });
        return result;
    }

    function generateErrorMessage(error) {
        if("AlreadySelectedLastTime" === error.code) {
            return "上次已经举办过\"" + error.detail.name + "\"活动，本次不可选择！";
        }

        if("MutexActivity" === error.code) {
            return "在[" + error.detail.packageName + "]中，\'" + error.detail.currentActivityName + "\'和\'" + error.detail.mutexActivityName + "\'不能同时选择！"
        }
    }

    function cancelActivity(packageItemId, activityItemId) {
        var requestBody = {
            teamBuildingPackageItemId: packageItemId,
            activityItemId: activityItemId,
        };

        $.ajax({
            type: "POST",
            url: "/api-maintenance/after?sAction=unselect",
            data: requestBody,
            async: false,
            success: function () {
                console.log('发送"取消活动"请求成功:' + JSON.stringify(requestBody));
            }
        });
    }
</script>
</body>
</html>
