package com.tw.api_mantenance.after.domain.service;

import com.tw.api_maintenance.after.domain.entity.ActivityItem;
import com.tw.api_maintenance.after.domain.entity.TeamBuildingPackageItem;
import com.tw.api_maintenance.after.domain.error_handling.ErrorDetail;
import com.tw.api_maintenance.after.domain.error_handling.DomainErrorName;
import com.tw.api_maintenance.after.domain.exception.SelectActivityException;
import com.tw.api_maintenance.after.domain.repository.IActivityDependentRepository;
import com.tw.api_maintenance.after.domain.service.TeamBuildingDomainService;
import com.tw.api_maintenance.after.domain.service.TeamBuildingPackageItemValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamBuildingDomainServiceTest {

    private TeamBuildingPackageItemValidationService mockTeamBuildingPackageItemValidationService;
    private IActivityDependentRepository mockActivityDependentRepository;
    private TeamBuildingDomainService teamBuildingDomainService;

    @Before
    public void init() {
        mockTeamBuildingPackageItemValidationService  = mock(TeamBuildingPackageItemValidationService.class);
        mockActivityDependentRepository = mock(IActivityDependentRepository.class);
        teamBuildingDomainService = new TeamBuildingDomainService(mockTeamBuildingPackageItemValidationService, mockActivityDependentRepository);
    }

    @Test
    public void testSelectActivityItem() throws SelectActivityException {
        long teamBuildingPackageItemId = 1234223L;
        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        doNothing().when(mockTeamBuildingPackageItemValidationService).validate(teamBuildingPackageItem, 12L, 10);

        teamBuildingDomainService.selectActivityItem(teamBuildingPackageItem, 12L, 10);

        verify(mockTeamBuildingPackageItemValidationService, times(1)).validate(any(), any(), any());

        assertEquals(teamBuildingPackageItemId, teamBuildingPackageItem.getId().longValue());
        assertEquals(101L, teamBuildingPackageItem.getPackageId().longValue());
        assertEquals(2, teamBuildingPackageItem.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), teamBuildingPackageItem.getDate().toString());
        assertFalse(teamBuildingPackageItem.isCompleted());

        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertEquals(5, activityItem.getCount().intValue());
        assertTrue(activityItem.getSelected());

        activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertEquals(10, activityItem.getCount().intValue());
        assertTrue(activityItem.getSelected());
    }

    @Test
    public void testSelectActivityItemWhenValidateFailed() throws SelectActivityException {
        long teamBuildingPackageItemId = 1234223L;
        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        DomainErrorName expectedDomainErrorName = DomainErrorName.NotInRange;
        ErrorDetail expectedErrorDetail = new ErrorDetail();

        doThrow(new SelectActivityException(expectedDomainErrorName, expectedErrorDetail)).when(mockTeamBuildingPackageItemValidationService).validate(teamBuildingPackageItem, 12L, 10);

        SelectActivityException actualException = null;
        try {
            teamBuildingDomainService.selectActivityItem(teamBuildingPackageItem, 12L, 10);
        } catch(SelectActivityException ex) {
            actualException = ex;
        }

        assertEquals(expectedDomainErrorName, actualException.getErrorName());
        assertEquals(expectedErrorDetail, actualException.getErrorDetail());
    }

    @Test
    public void testUnselectActivityItemWhenNoDependent() {
        long teamBuildingPackageItemId = 1234223L;

        TeamBuildingPackageItem teamBuildingPackageItem = new TeamBuildingPackageItem(teamBuildingPackageItemId, 101L, Arrays.asList(
                new ActivityItem(11L, 13L, true, 5),
                new ActivityItem(12L, 14L, false, null)), new Date(2022, 2, 1), false);

        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(null);

        Long unselectedDependentActivityId = teamBuildingDomainService.unSelectActivityItem(teamBuildingPackageItem, 11L);
        assertNull(unselectedDependentActivityId);

        assertEquals(teamBuildingPackageItemId, teamBuildingPackageItem.getId().longValue());
        assertEquals(101L, teamBuildingPackageItem.getPackageId().longValue());
        assertEquals(2, teamBuildingPackageItem.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), teamBuildingPackageItem.getDate().toString());
        assertFalse(teamBuildingPackageItem.isCompleted());

        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
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

        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(14L);

        Long unselectedDependentActivityId = teamBuildingDomainService.unSelectActivityItem(teamBuildingPackageItem, 11L);
        assertNull(unselectedDependentActivityId);

        assertEquals(teamBuildingPackageItemId, teamBuildingPackageItem.getId().longValue());
        assertEquals(101L, teamBuildingPackageItem.getPackageId().longValue());
        assertEquals(2, teamBuildingPackageItem.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), teamBuildingPackageItem.getDate().toString());
        assertFalse(teamBuildingPackageItem.isCompleted());

        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
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

        when(mockActivityDependentRepository.findByDependentId(101L, 13L)).thenReturn(14L);

        Long unselectedDependentActivityId = teamBuildingDomainService.unSelectActivityItem(teamBuildingPackageItem, 11L);
        assertEquals(12L, unselectedDependentActivityId.longValue());

        assertEquals(teamBuildingPackageItemId, teamBuildingPackageItem.getId().longValue());
        assertEquals(101L, teamBuildingPackageItem.getPackageId().longValue());
        assertEquals(2, teamBuildingPackageItem.getActivityItems().size());
        assertEquals(new Date(2022, 2, 1).toString(), teamBuildingPackageItem.getDate().toString());
        assertFalse(teamBuildingPackageItem.isCompleted());

        ActivityItem activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 11L).findFirst().get();
        assertEquals(13L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());

        activityItem = teamBuildingPackageItem.getActivityItems().stream().filter(i -> i.getId() == 12L).findFirst().get();
        assertEquals(14L, activityItem.getActivityId().longValue());
        assertNull(activityItem.getCount());
        assertFalse(activityItem.getSelected());
    }
}
