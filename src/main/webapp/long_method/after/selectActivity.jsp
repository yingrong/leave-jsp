<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    class Entity {
        private long id;
        private String name;

        public Entity(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    Entity teamBuildingPackage = new Entity(10010, "十万用户团建礼包");
    List<Entity> activities = Arrays.asList(
            new Entity(1, "冬奥两日游"),//旅游类活动
            new Entity(2, "户外探险一日游"),//旅游类活动
            new Entity(3, "唱歌"),
            new Entity(4, "吃饭"),
            new Entity(5, "住宿")//只存在于两日游项目
    );

%>

<html>
<head>
    <title>团建活动选择（长函数拆分）</title>
</head>
<body>
<h1 id="product_<%=teamBuildingPackage.getId()%>"><%=teamBuildingPackage.getName()%>
</h1>
<h2>团建活动列表</h2>
<table border="1px">
    <tr>
        <th>选择</th>
        <th>活动</th>
        <th>参加人数</th>
    </tr>
    <%for (int i = 0; i < activities.size(); i++) {%>
    <tr>
        <td><input id="activity_<%=activities.get(i).getId()%>" type="checkbox"
                   onclick="clickActivityCheck(this, <%=teamBuildingPackage.getId()%>, <%=activities.get(i).getId()%>)">
        </td>
        <td><span><%=activities.get(i).getName()%></span></td>
        <td><input id="activity_<%=activities.get(i).getId()%>_count"></td>
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

<script>
    var packageConfig = {
        10010: "十万用户团建礼包",
        10086: "百万用户团建礼包"
    };

    var activityConfig = {
        1: "冬奥两日游",
        2: "户外探险一日游",
        3: "唱歌",
        4: "吃饭",
        5: "住宿",
        11: "自由三日飞"
    };

    var mutexActivityConfig = {
        10010: {
            1: 2,
            2: 1
        },
        10086: {
            1: 11,
            11: 1
        }
    };

    function getMutexActivity(packageId, activityId) {
        return mutexActivityConfig[packageId] && mutexActivityConfig[packageId][activityId]
    }

    function getPackageName(packageId) {
        return packageConfig[packageId];
    }

    function getActivityName(activityId) {
        return activityConfig[activityId];
    }

    function validateMutexActivities(activityCheckedObject, packageId, activityId) {
        var mutexActivityId = getMutexActivity(packageId, activityId);
        if (mutexActivityId) {
            var checkBox = document.getElementById("activity_" + mutexActivityId);
            if (checkBox && checkBox.checked) {
                alert("在" + getPackageName(packageId) + "中，" + getActivityName(activityId) + "和" + getActivityName(mutexActivityId) + "不能同时选择！");
                activityCheckedObject.checked = false;
                return false;
            }
        }

        return true;
    }

    function clickActivityCheck(activityCheckedObject, packageId, activityId) {
        if (activityCheckedObject.checked) {
            var countInput = document.getElementById("activity_" + activityId + "_count");
            var count = !countInput.value ? -1 : parseInt(countInput.value);
            if (count < 1 || count > 50) {
                alert("参加人数必须在1到50之间！");
                activityCheckedObject.checked = false;
                return;
            }
            var validateMutexActivityResult = validateMutexActivities(activityCheckedObject, packageId, activityId);

            if (!validateMutexActivityResult) {
                return;
            }

            if (packageId === 10010 && activityId === 5) {
                var checkBox1 = document.getElementById("activity_1");
                if (!checkBox1 || !checkBox1.checked) {
                    alert("在十万用户团建礼包中，选择住宿前必须选择冬奥两日游！");
                    activityCheckedObject.checked = false;
                    return;
                }
            }

            var result = createActivity(activityCheckedObject, packageId, activityId);
            if (!result.success) {
                alert(result.errorMessage);
                activityCheckedObject.checked = false;
                return;
            }
        } else {
            var countInput = document.getElementById("activity_" + activityId + "_count");
            countInput.value = "";
            activityCheckedObject.checked = false;

            if (packageId === 10010 && activityId === 1) {
                var checkBox5 = document.getElementById("activity_5");
                if (checkBox5 && checkBox5.checked) {
                    checkBox5.click();
                }
            }

            if (packageId === 10086 && activityId === 999) {
                var checkBox12 = document.getElementById("activity_12");
                if (checkBox12 && checkBox12.checked) {
                    checkBox12.click();
                }
            }

            cancelActivity(activityCheckedObject, packageId, activityId);
        }
    }

    function createActivity(activityCheckedObject, packageId, activityId) {
        if (packageId == 10010 && activityId == 3) {
            console.log('发送请求。创建活动失败：' + activityId);

            return {
                success: false,
                errorMessage: "上次已经举办过唱歌活动，本次不可选择！"
            }
        }

        console.log('发送请求。创建活动成功：' + activityId);
        return {success: true};
    }

    function cancelActivity(activityCheckedObject, packageId, activityId) {
        console.log('发送请求。取消活动成功:' + activityId);
    }
</script>
</body>
</html>
