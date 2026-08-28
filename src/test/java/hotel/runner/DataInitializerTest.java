package hotel.runner;

import hotel.repositories.RoomCategoryRepository;
import hotel.repositories.RoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class DataInitializerTest {

    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test per il Metodo run()
    @Test
    public void testRun() {
        dataInitializer.run();

        verify(roomCategoryRepository, times(1)).saveAll(anyList());

        verify(roomRepository, times(1)).saveAll(anyList());
    }
}
