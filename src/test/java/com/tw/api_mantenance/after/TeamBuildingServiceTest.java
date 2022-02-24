package com.tw.api_mantenance.after;

import com.tw.api_maintenance.after.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingServiceTest {

    TeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;
    TeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    ActivityRepository mockActivityRepository;
    TeamBuildingService teamBuildingService;

    @Before
    void init() {
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

//        private Long id;
//        private Long packageId;
//        private String name;
//        private List<ActivityItemDto> activityItemDtos;

//        private Long id;
//        private Long activityId;
//        private String name;
//        private Integer count;
//        private Boolean selected;

        Assert.assertEquals(teamBuildingPackageItemId, teamBuildingPackageItemDto.getId().longValue());
        Assert.assertEquals(101L, teamBuildingPackageItemDto.getPackageId().longValue());

        Assert.assertTrue(true);
    }
}
