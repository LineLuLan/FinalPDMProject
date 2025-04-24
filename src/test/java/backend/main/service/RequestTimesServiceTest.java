/*
package backend.main.service;

import backend.main.model.RequestTimes;
import backend.main.repository.RequestTimesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestTimesServiceTest {

    @Mock
    private RequestTimesRepository requestTimesRepository;

    @InjectMocks
    private RequestTimesService requestTimesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRequestTimesByPatientId() {
        RequestTimes rt1 = new RequestTimes();
        rt1.setRequestId(1);
        rt1.setPatientId(100);
        rt1.setRequestDate(LocalDateTime.now());
        RequestTimes rt2 = new RequestTimes();
        rt2.setRequestId(2);
        rt2.setPatientId(100);
        rt2.setRequestDate(LocalDateTime.now());
        List<RequestTimes> mockList = Arrays.asList(rt1, rt2);
        when(requestTimesRepository.findByPatientId(100)).thenReturn(mockList);
        List<RequestTimes> result = requestTimesService.getRequestTimesByPatientId(100);
        assertEquals(2, result.size());
        verify(requestTimesRepository, times(1)).findByPatientId(100);
    }

    @Test
    void testAddRequestTimes() {
        RequestTimes rt = new RequestTimes();
        when(requestTimesRepository.save(rt)).thenReturn(1);
        int result = requestTimesService.addRequestTimes(rt);
        assertEquals(1, result);
        verify(requestTimesRepository, times(1)).save(rt);
    }

    @Test
    void testDeleteRequestTimesByPatientId() {
        when(requestTimesRepository.deleteByPatientId(100)).thenReturn(1);
        int result = requestTimesService.deleteRequestTimesByPatientId(100);
        assertEquals(1, result);
        verify(requestTimesRepository, times(1)).deleteByPatientId(100);
    }
}
*/
