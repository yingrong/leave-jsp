package com.tw.api_mantenance.after.domain.service;

import com.tw.api_maintenance.after.domain.entity.Activity;
import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackage;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.*;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.*;
import com.tw.api_maintenance.after.domain.service.TeamBuildingPackageItemValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingPackageItemValidationServiceTest {

    private TeamBuildingPackageItemValidationService teamBuildingPackageItemValidationService;
    private IActivityDependentRepository mockActivityDependentRepository;
    private ITeamBuildingPackageRepository mockTeamBuildingPackageRepository;
    private IActivityRepository mockActivityRepository;
    private IActivityMutexRepository mockActivityMutexRepository;
    private ITeamBuildingPackageItemRepository mockTeamBuildingPackageItemRepository;

    @Captor
    ArgumentCaptor<List<Long>> longListCaptor;

    @Before
    public void init() {
        mockActivityDependentRepository = mock(IActivityDependentRepository.class);
        mockTeamBuildingPackageRepository = mock(ITeamBuildingPackageRepository.class);
        mockActivityRepository = mock(IActivityRepository.class);
        mockActivityMutexRepository = mock(IActivityMutexRepository.class);
        mockTeamBuildingPackageItemRepository = mock(ITeamBuildingPackageItemRepository.class);

        teamBuildingPackageItemValidationService = new TeamBuildingPackageItemValidationService(mockTeamBuildingPackageItemRepository, mockTeamBuildingPackageRepository,
                mockActivityRepository, mockActivityMutexRepository, mockActivityDependentRepository);
    }

    @Test
    public void testSelectActivityItemWhenMutexSelected() {
        long teamBuildingPackageItemId = 1234223L;
        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null),
                new ActivityItem(111L, 112L, true, 6)), new Date(2022, 2, 1), false);

        when(mockActivityDependentRepository.findByReliedId(101L, 14L)).thenReturn(null);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "测试团建包名字", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(anyListOf(Long.class))).thenReturn(Arrays.asList(
                new Activity(14L, "14L活动名称"), new Activity(112L, "112L活动名称")));
        when(mockActivityMutexRepository.findByMutexActivityId(101L, 14L)).thenReturn(112L);

        SelectActivityException expectedException = null;
        try {
            teamBuildingPackageItemValidationService.validate(teamBuildingPackageItem, 12L, 8);
        } catch (SelectActivityException e) {
            expectedException = e;
        }

        assertEquals(DomainErrorName.MutexActivity, expectedException.getErrorName());
        MutexActivityErrorDetail detail = (MutexActivityErrorDetail)expectedException.getErrorDetail();

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

        when(mockActivityDependentRepository.findByReliedId(101L, 14L)).thenReturn(13L);
        when(mockTeamBuildingPackageRepository.findById(101L)).thenReturn(new TeamBuildingPackage(101L, "当前的团建包", Arrays.asList(13L, 14L)));
        when(mockActivityRepository.findByIds(Arrays.asList(14L, 13L))).thenReturn(Arrays.asList(
                new Activity(13L, "被依赖的活动"),
                new Activity(14L, "当前的活动")));



        SelectActivityException expectedException = null;
        try {
            teamBuildingPackageItemValidationService.validate(teamBuildingPackageItem, 12L, 8);
        } catch (SelectActivityException e) {
            expectedException = e;
        }

        assertEquals(DomainErrorName.ReliedNotSelected, expectedException.getErrorName());
        ReliedNotSelectedErrorDetail detail = (ReliedNotSelectedErrorDetail)expectedException.getErrorDetail();

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

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), true);

        when(mockActivityDependentRepository.findByReliedId(101L, 13L)).thenReturn(null);
        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);
        when(mockActivityRepository.findByIds(Arrays.asList(13L))).thenReturn(Arrays.asList(new Activity(13L, "ABC")));


        SelectActivityException expectedException = null;
        try {
            teamBuildingPackageItemValidationService.validate(teamBuildingPackageItem, 11L, 8);
        } catch (SelectActivityException e) {
            expectedException = e;
        }

        assertEquals(DomainErrorName.AlreadySelectedLastTime, expectedException.getErrorName());
        AlreadySelectedLastTimeErrorDetail detail = (AlreadySelectedLastTimeErrorDetail)expectedException.getErrorDetail();

        assertEquals(11L, detail.getActivityItemId().longValue());
        assertEquals(13L, detail.getActivityId().longValue());
        assertEquals("ABC", detail.getName());
    }

    @Test
    public void testSelectActivityItemWhenTheCountIsNOTBetween1and50() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        TeamBuildingPackageItem completedTeamBuildingPackageItem = new TeamBuildingPackageItem(2329L, 101L, Arrays.asList(
                new ActivityItem(223L, 13L, true, 5)), new Date(2022, 1, 3), false);

        when(mockTeamBuildingPackageItemRepository.findLastCompleted()).thenReturn(completedTeamBuildingPackageItem);


        List<Integer> requestCounts = Arrays.asList(0, 51, -2);

        for (Integer requestCount : requestCounts) {

            SelectActivityException expectedException = null;
            try {
                teamBuildingPackageItemValidationService.validate(teamBuildingPackageItem, 12L, requestCount);
            } catch (SelectActivityException e) {
                expectedException = e;
            }

            assertEquals(DomainErrorName.NotInRange, expectedException.getErrorName());
            NotInRangeErrorDetail detail = (NotInRangeErrorDetail)expectedException.getErrorDetail();

            assertEquals(requestCount, detail.getInputValue());
            assertEquals(50, detail.getMax().intValue());
            assertEquals(1, detail.getMin().intValue());
        }
    }

}
