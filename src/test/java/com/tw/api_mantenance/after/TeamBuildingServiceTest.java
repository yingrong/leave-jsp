package com.tw.api_mantenance.after;

import com.tw.api_maintenance.after.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingServiceTest {

    TeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;
    TeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    ActivityRepository mockActivityRepository;
    TeamBuildingService teamBuildingService;

    @Before
    public void init() {
        mockTeamBuildingPackageItemRepository = mock(TeamBuildingPackageItemRepository.class);
        mockTeamBuildingPackageRepository = mock(TeamBuildingPackageRepository.class);
        mockActivityRepository = mock(ActivityRepository.class);
        teamBuildingService = new TeamBuildingService(mockTeamBuildingPackageItemRepository, mockTeamBuildingPackageRepository, mockActivityRepository);
    }

    @Test
    public void testQueryTeamBuildingPackageItemById() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)));

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "团建测试包", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(Arrays.asList(13L, 14L))).thenReturn(Arrays.asList(
                new Activity(13L, "测试活动"),
                new Activity(14L, "第二个测试活动")
        ));

        TeamBuildingPackageItemDto teamBuildingPackageItemDto = teamBuildingService.queryTeamBuildingPackage(teamBuildingPackageItemId);

        Assert.assertEquals(teamBuildingPackageItemId, teamBuildingPackageItemDto.getId().longValue());
        Assert.assertEquals(101L, teamBuildingPackageItemDto.getPackageId().longValue());
        Assert.assertEquals("团建测试包", teamBuildingPackageItemDto.getName());

        Assert.assertEquals(2, teamBuildingPackageItemDto.getActivityItemDtos().size());

        ActivityItemDto activityItemDto = teamBuildingPackageItemDto.getActivityItemDtos().stream().filter(i -> i.getId() == 11L).findFirst().get();
        Assert.assertEquals(13L, activityItemDto.getActivityId().longValue());
        Assert.assertEquals("测试活动", activityItemDto.getName());
        Assert.assertEquals(5, activityItemDto.getCount().intValue());
        Assert.assertTrue(activityItemDto.getSelected());

        activityItemDto = teamBuildingPackageItemDto.getActivityItemDtos().stream().filter(i -> i.getId() == 12L).findFirst().get();
        Assert.assertEquals(14L, activityItemDto.getActivityId().longValue());
        Assert.assertEquals("第二个测试活动", activityItemDto.getName());
        Assert.assertNull(activityItemDto.getCount());
        Assert.assertFalse(activityItemDto.getSelected());
    }

    @Test
    public void testSelectActivityItem() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)));

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, 10);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        Assert.assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        Assert.assertEquals(101L, captorValue.getPackageId().longValue());
        Assert.assertEquals(2, captorValue.getActivityItems().size());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        Assert.assertEquals(13L, activityItem.getActivityId().longValue());
        Assert.assertEquals(5, activityItem.getCount().intValue());
        Assert.assertTrue(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        Assert.assertEquals(14L, activityItem.getActivityId().longValue());
        Assert.assertEquals(10, activityItem.getCount().intValue());
        Assert.assertTrue(activityItem.getSelected());
    }

    @Test
    public void testUnselectActivityItem() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)));

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(argumentCaptor.capture());

        TeamBuildingPackageItem captorValue = argumentCaptor.getValue();
        Assert.assertEquals(teamBuildingPackageItemId, captorValue.getId().longValue());
        Assert.assertEquals(101L, captorValue.getPackageId().longValue());
        Assert.assertEquals(2, captorValue.getActivityItems().size());

        ActivityItem activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        Assert.assertEquals(13L, activityItem.getActivityId().longValue());
        Assert.assertNull(activityItem.getCount());
        Assert.assertFalse(activityItem.getSelected());

        activityItem = captorValue.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        Assert.assertEquals(14L, activityItem.getActivityId().longValue());
        Assert.assertNull(activityItem.getCount());
        Assert.assertFalse(activityItem.getSelected());
    }
}
