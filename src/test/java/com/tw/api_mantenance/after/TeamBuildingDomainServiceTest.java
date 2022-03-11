package com.tw.api_mantenance.after;

import com.tw.api_maintenance.after.domain.error_handling.*;
import com.tw.api_maintenance.after.application.ActivityItemDto;
import com.tw.api_maintenance.after.application.TeamBuildingPackageItemDto;
import com.tw.api_maintenance.after.application.TeamBuildingService;
import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.Error;
import com.tw.api_maintenance.after.infrastructure.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingDomainServiceTest {

    TeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;
    TeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    ActivityRepository mockActivityRepository;
    TeamBuildingService teamBuildingService;
    ActivityMutexRepository mockActivityMutexRepository;
    ActivityDependentRepository mockActivityDependentRepository;

    @Captor
    ArgumentCaptor<List<Long>> longListCaptor;

    @Before
    public void init() {
        mockTeamBuildingPackageItemRepository = mock(TeamBuildingPackageItemRepository.class);
        mockTeamBuildingPackageRepository = mock(TeamBuildingPackageRepository.class);
        mockActivityRepository = mock(ActivityRepository.class);
        mockActivityMutexRepository = mock(ActivityMutexRepository.class);
        mockActivityDependentRepository = mock(ActivityDependentRepository.class);
        teamBuildingService = new TeamBuildingService(mockTeamBuildingPackageItemRepository, mockTeamBuildingPackageRepository, mockActivityRepository, mockActivityMutexRepository, mockActivityDependentRepository);
    }

    @Test
    public void testQueryTeamBuildingPackageItemById() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2021, 5, 1), true);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "团建测试包", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(Arrays.asList(13L, 14L))).thenReturn(Arrays.asList(
                new Activity(13L, "测试活动"),
                new Activity(14L, "第二个测试活动")
        ));

        TeamBuildingPackageItemDto teamBuildingPackageItemDto = teamBuildingService.queryTeamBuildingPackage(teamBuildingPackageItemId);

        assertEquals(teamBuildingPackageItemId, teamBuildingPackageItemDto.getId().longValue());
        assertEquals(101L, teamBuildingPackageItemDto.getPackageId().longValue());
        assertEquals("团建测试包", teamBuildingPackageItemDto.getName());
        assertEquals(new Date(2021, 5, 1).toString(), teamBuildingPackageItemDto.getDate().toString());
        assertTrue(teamBuildingPackageItemDto.isCompleted());

        assertEquals(2, teamBuildingPackageItemDto.getActivityItemDtos().size());

        ActivityItemDto activityItemDto = teamBuildingPackageItemDto.getActivityItemDtos().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItemDto.getActivityId().longValue());
        assertEquals("测试活动", activityItemDto.getName());
        assertEquals(5, activityItemDto.getCount().intValue());
        assertTrue(activityItemDto.getSelected());

        activityItemDto = teamBuildingPackageItemDto.getActivityItemDtos().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItemDto.getActivityId().longValue());
        assertEquals("第二个测试活动", activityItemDto.getName());
        assertNull(activityItemDto.getCount());
        assertFalse(activityItemDto.getSelected());
    }

    @Test
    public void testSelectActivityItem() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), false);

        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);
        when(mockActivityDependentRepository.findByReliedId(101L, 14L)).thenReturn(null);
        when(mockActivityMutexRepository.findByMutexActivityId(101L, 14L)).thenReturn(null);
        Error error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, "10");
        assertNull(error);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        assertEquals(101L, captorValue.getPackageId().longValue());
        assertEquals(2, captorValue.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), captorValue.getDate().toString());
        assertFalse(captorValue.isCompleted());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertEquals(5, activityItem.getCount().intValue());
        assertTrue(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertEquals(10, activityItem.getCount().intValue());
        assertTrue(activityItem.getSelected());
    }

    @Test
    public void testSelectActivityItemWhenMutexSelected() {
        long teamBuildingPackageItemId = 1234223L;
        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null),
                new ActivityItem(111L, 112L, true, 6)), new Date(2022, 2, 1), false);

        when(mockActivityDependentRepository.findByReliedId(101L, 14L)).thenReturn(null);
        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "测试团建包名字", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(anyListOf(Long.class))).thenReturn(Arrays.asList(
                new Activity(14L, "14L活动名称"), new Activity(112L, "112L活动名称")));
        when(mockActivityMutexRepository.findByMutexActivityId(101L, 14L)).thenReturn(112L);

        Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, "8");

        assertEquals(ErrorName.MutexActivity.getCode(), error.getCode());
        assertEquals(ErrorName.MutexActivity.getDescription(), error.getDescription());

        MutexActivityErrorDetail detail = (MutexActivityErrorDetail)error.getDetail();
        assertEquals(teamBuildingPackageItemId, detail.getPackageItemId().longValue());
        assertEquals(101L, detail.getPackageId().longValue());
        assertEquals("测试团建包名字", detail.getPackageName());
        assertEquals(12L, detail.getCurrentActivityItemId().longValue());
        assertEquals(14L, detail.getCurrentActivityId().longValue());
        assertEquals("14L活动名称", detail.getCurrentActivityName());
        assertEquals(112L, detail.getMutexActivityId().longValue());
        assertEquals("112L活动名称", detail.getMutexActivityName());

        verify(mockTeamBuildingPackageRepository, times(1)).findById(101L);
        verify(mockActivityRepository, times(1)).findByIds(longListCaptor.capture());
        verify(mockActivityMutexRepository, times(1)).findByMutexActivityId(101L, 14L);

        List<Long> activityIds = longListCaptor.getValue();
        assertEquals(2, activityIds.size());
        assertTrue(activityIds.contains(14L));
        assertTrue(activityIds.contains(112L));

    }

    @Test
    public void testSelectActivityItemWhenReliedIsNotSelected() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, false, null),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        when(mockActivityDependentRepository.findByReliedId(101L, 14L)).thenReturn(13L);

        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "当前的团建包", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(Arrays.asList(14L, 13L))).thenReturn(Arrays.asList(
                        new Activity(13L, "被依赖的活动"),
                        new Activity(14L, "当前的活动")));


        Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, "10");
        assertEquals(ErrorName.ReliedNotSelected.getCode(), error.getCode());
        assertEquals(ErrorName.ReliedNotSelected.getDescription(), error.getDescription());

        ReliedNotSelectedErrorDetail detail = (ReliedNotSelectedErrorDetail)error.getDetail();
        assertEquals(teamBuildingPackageItemId, detail.getPackageItemId().longValue());
        assertEquals(101L, detail.getPackageId().longValue());
        assertEquals("当前的团建包", detail.getPackageName());
        assertEquals(12L, detail.getCurrentActivityItemId().longValue());
        assertEquals(14L, detail.getCurrentActivityId().longValue());
        assertEquals("当前的活动", detail.getCurrentActivityName());
        assertEquals(13L, detail.getReliedActivityId().longValue());
        assertEquals("被依赖的活动", detail.getReliedActivityName());
    }

    @Test
    public void testSelectActivityItemFailedWhenTheActivityIsSelectedLastTime() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, false, null),
                new ActivityItem(12L, 14L, false, null)));

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), true);

        when(mockActivityDependentRepository.findByReliedId(101L, 13L)).thenReturn(null);
        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);

        when(mockActivityRepository.findByIds(Arrays.asList(13L))).thenReturn(Arrays.asList(new Activity(13L, "ABC")));

        Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 11L, "10");
        assertEquals(ErrorName.AlreadySelectedLastTime.getCode(), error.getCode());
        assertEquals(ErrorName.AlreadySelectedLastTime.getDescription(), error.getDescription());

        AlreadySelectedLastTimeErrorDetail detail = (AlreadySelectedLastTimeErrorDetail)error.getDetail();
        assertEquals(11L, detail.getActivityItemId().longValue());
        assertEquals(13L, detail.getActivityId().longValue());
        assertEquals("ABC", detail.getName());
    }

    @Test
    public void testSelectActivityItemWhenTheCountIsNOTExpectedType() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), false);

        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);


        List<String> requestCounts = Arrays.asList("abc", "@", "怪异");

        for (String requestCount : requestCounts) {
            Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, requestCount);

            assertEquals(ErrorName.UnexpectedType.getCode(), error.getCode());
            assertEquals(ErrorName.UnexpectedType.getDescription(), error.getDescription());

            UnexpectedTypeErrorDetail detail = (UnexpectedTypeErrorDetail) error.getDetail();
            assertEquals(requestCount, detail.getInputValue());
            assertEquals("java.lang.Integer", detail.getExpectedType());

            verify(mockTeamBuildingPackageItemRepository, times(0)).save(any());
        }
    }

    @Test
    public void testSelectActivityItemWhenTheCountIsNOTBetween1and50() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), false);

        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);


        List<String> requestCounts = Arrays.asList("0", "51", "-2");

        for (String requestCount : requestCounts) {
            Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, requestCount);

            assertEquals(ErrorName.NotInRange.getCode(), error.getCode());
            assertEquals(ErrorName.NotInRange.getDescription(), error.getDescription());

            NotInRangeErrorDetail detail = (NotInRangeErrorDetail) error.getDetail();
            assertEquals(Integer.parseInt(requestCount), detail.getInputValue().intValue());
            assertEquals(50, detail.getMax().intValue());
            assertEquals(1, detail.getMin().intValue());

            verify(mockTeamBuildingPackageItemRepository, times(0)).save(any());
        }
    }

    @Test
    public void testUnselectActivityItemWhenNoDependent() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(null);

        Long unselectedDependentActivityId = teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);
        assertNull(unselectedDependentActivityId);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        assertEquals(101L, captorValue.getPackageId().longValue());
        assertEquals(2, captorValue.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), captorValue.getDate().toString());
        assertFalse(captorValue.isCompleted());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());
    }

    @Test
    public void testUnselectActivityItemWhenDependentIsUnSelected() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(14L);

        Long unselectedDependentActivityId = teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);
        assertNull(unselectedDependentActivityId);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        assertEquals(101L, captorValue.getPackageId().longValue());
        assertEquals(2, captorValue.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), captorValue.getDate().toString());
        assertFalse(captorValue.isCompleted());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());
    }

    @Test
    public void testUnselectActivityItemWhenDependentIsSelected() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, true, 6)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(14L);

        Long unselectedDependentActivityId = teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);
        assertEquals(12L, unselectedDependentActivityId.longValue());

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        assertEquals(101L, captorValue.getPackageId().longValue());
        assertEquals(2, captorValue.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), captorValue.getDate().toString());
        assertFalse(captorValue.isCompleted());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());
    }
}
