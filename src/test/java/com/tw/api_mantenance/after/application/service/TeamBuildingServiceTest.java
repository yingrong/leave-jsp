package com.tw.api_mantenance.after.application.service;

import com.tw.api_maintenance.after.application.dto.ActivityItemDto;
import com.tw.api_maintenance.after.application.dto.TeamBuildingPackageItemDto;
import com.tw.api_maintenance.after.application.error_handling.ApplicationErrorName;
import com.tw.api_maintenance.after.application.error_handling.Error;
import com.tw.api_maintenance.after.application.service.TeamBuildingService;
import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;
import com.tw.api_maintenance.after.application.error_handling.UnexpectedTypeErrorDetail;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.*;
import com.tw.api_maintenance.after.domain.service.TeamBuildingDomainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingServiceTest {

    ITeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;
    ITeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    IActivityRepository mockActivityRepository;
    TeamBuildingDomainService mockTeamBuildingDomainService;
    TeamBuildingService teamBuildingService;

    @Before
    public void init() {
        mockTeamBuildingPackageItemRepository = mock(ITeamBuildingPackageItemRepository.class);
        mockTeamBuildingPackageRepository = mock(ITeamBuildingPackageRepository.class);
        mockActivityRepository = mock(IActivityRepository.class);
        mockTeamBuildingDomainService = mock(TeamBuildingDomainService.class);
        teamBuildingService = new TeamBuildingService(mockTeamBuildingPackageItemRepository, mockTeamBuildingPackageRepository,
                mockActivityRepository, mockTeamBuildingDomainService);
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
    public void testSelectActivityItem() throws SelectActivityException {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        doNothing().when(mockTeamBuildingDomainService).selectActivityItem(teamBuildingPackageItem, 12L, 10);

        Error error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, "10");
        assertNull(error);

        verify(mockTeamBuildingPackageItemRepository, times(1)).findById(teamBuildingPackageItemId);
        verify(mockTeamBuildingDomainService, times(1)).selectActivityItem(teamBuildingPackageItem, 12L, 10);

        ArgumentCaptor<TeamBuildingPackageItem> argumentCaptor = ArgumentCaptor.forClass(TeamBuildingPackageItem.class);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(teamBuildingPackageItem);
    }

    @Test
    public void testSelectActivityItemWhenTheCountIsNOTExpectedType() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

//        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), false);

//        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);


        List<String> requestCounts = Arrays.asList("abc", "@", "怪异");

        for (String requestCount : requestCounts) {
            Error<? extends ErrorDetail> error = teamBuildingService.selectActivityItem(teamBuildingPackageItemId, 12L, requestCount);

            assertEquals(ApplicationErrorName.UnexpectedType.getCode(), error.getCode());
            assertEquals(ApplicationErrorName.UnexpectedType.getDescription(), error.getDescription());

            UnexpectedTypeErrorDetail detail = (UnexpectedTypeErrorDetail) error.getDetail();
            assertEquals(requestCount, detail.getInputValue());
            assertEquals("java.lang.Integer", detail.getExpectedType());

            verify(mockTeamBuildingPackageItemRepository, times(0)).save(any());
        }
    }

    @Test
    public void testUnselectActivityItem() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockTeamBuildingPackageItemRepository.findById(teamBuildingPackageItemId)).thenReturn(teamBuildingPackageItem);
        when(mockTeamBuildingDomainService.unSelectActivityItem(teamBuildingPackageItem, 11L)).thenReturn(12345L);

        Long unselectedDependentActivityId = teamBuildingService.unSelectActivityItem(teamBuildingPackageItemId, 11L);

        assertEquals(12345L, unselectedDependentActivityId.longValue());
        verify(mockTeamBuildingPackageItemRepository, times(1)).findById(teamBuildingPackageItemId);
        verify(mockTeamBuildingDomainService, times(1)).unSelectActivityItem(teamBuildingPackageItem, 11L);
        verify(mockTeamBuildingPackageItemRepository, times(1)).save(teamBuildingPackageItem);
    }
}
