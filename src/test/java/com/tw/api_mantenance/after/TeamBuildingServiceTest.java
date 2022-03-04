package com.tw.api_mantenance.after;

import com.tw.api_maintenance.after.Error;
import com.tw.api_maintenance.after.*;
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
public class TeamBuildingServiceTest {

    TeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;
    TeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    ActivityRepository mockActivityRepository;
    TeamBuildingService teamBuildingService;
    ActivityMutexRepository activityMutexRepository;

    @Captor
    ArgumentCaptor<List<Long>> longListCaptor;

    @Before
    public void init() {
        mockTeamBuildingPackageItemRepository = mock(TeamBuildingPackageItemRepository.class);
        mockTeamBuildingPackageRepository = mock(TeamBuildingPackageRepository.class);
        mockActivityRepository = mock(ActivityRepository.class);
        activityMutexRepository = mock(ActivityMutexRepository.class);
        teamBuildingService = new TeamBuildingService(mockTeamBuildingPackageItemRepository, mockTeamBuildingPackageRepository, mockActivityRepository, activityMutexRepository);
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


        Error error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, 10);
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
    public void testSelectActivityItemFailedWhenTheActivityIsSelectedLastTime() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, false, null),
                new ActivityItem(12L, 14L, false, null)));

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), true);

        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);

        when(mockActivityRepository.findByIds(Arrays.asList(13L))).thenReturn(Arrays.asList(new Activity(13L, "ABC")));

        Error error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 11L, 10);
        assertEquals(error.getMessage(),"上次已经举办过ABC活动，本次不可选择！");
    }

    @Test
    public void testUnselectActivityItem() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);

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
    public void testCheckMutex() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null),
                new ActivityItem(111L, 112L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "测试团建包名字", Arrays.asList(13L, 14L)));
        when(activityMutexRepository.findByMutexActivityId(101L, 14L)).thenReturn(112L);


        HashMap<String, String> result = teamBuildingService.checkMutexActivity(teamBuildingPackageItemId, 12L);
        assertNull(result);

        verify(mockTeamBuildingPackageRepository, times(0)).findById(anyLong());
        verify(mockActivityRepository, times(0)).findByIds(anyListOf(Long.class));
        verify(activityMutexRepository, times(1)).findByMutexActivityId(101L, 14L);
    }

    @Test
    public void testCheckMutexWhenCurrentMutexIsSelected() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null),
                new ActivityItem(111L, 112L, true, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "测试团建包名字", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(anyListOf(Long.class))).thenReturn(Arrays.asList(
        new Activity(14L, "14L活动名称"), new Activity(112L, "112L活动名称")));
        when(activityMutexRepository.findByMutexActivityId(101L, 14L)).thenReturn(112L);

        HashMap<String, String> result = teamBuildingService.checkMutexActivity(teamBuildingPackageItemId, 12L);
        assertTrue(result.containsKey("errorMessage"));
        assertEquals("在测试团建包名字中，14L活动名称和112L活动名称不能同时选择！", result.get("errorMessage"));

        verify(mockTeamBuildingPackageRepository, times(1)).findById(101L);
        verify(mockActivityRepository, times(1)).findByIds(longListCaptor.capture());
        verify(activityMutexRepository, times(1)).findByMutexActivityId(101L, 14L);

        List<Long> activityIds = longListCaptor.getValue();
        assertEquals(2, activityIds.size());
        assertTrue(activityIds.contains(14L));
        assertTrue(activityIds.contains(112L));
    }
}
