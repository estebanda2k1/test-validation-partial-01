package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BaggageFeeCalculatorTest {
    @Mock
     private PassengerService passengerService;
    @InjectMocks
    private BaggageFeeCalculator baggageFeeCalculator;
    
    @Test
    @DisplayName("Debe cargar $30 con una maleta normal")
    void debeCargar30_conMaletaNormalTest(){
        //Arrange
        long passengerId=1L;
        when(passengerService.isVip(passengerId)).thenReturn(false);

        //Test
        double fee= baggageFeeCalculator.calculateFee(20.0,1,passengerId);

        //Assert
        assertEquals(30.0, fee,0.001);
        verify(passengerService,times(1)).isVip(passengerId);
    }

    @Test
    @DisplayName("Debe salir $80 con exceso de peso")
    void debeSalir80_excesoDePesoTest(){
        // Arrange
        long passengerId = 2L;
        when(passengerService.isVip(passengerId)).thenReturn(false);

        // Act
        double fee = baggageFeeCalculator.calculateFee(25.0, 1, passengerId);

        // Assert
        assertEquals(80.0, fee, 0.001);
    }

    @Test
    @DisplayName("Debe salir $0 con maleta de usuario VIP dentro del peso por debajo del limite")
    void debeSalir0_maletaVipTest(){
        // Arrange
        long passengerId = 3L;
        when(passengerService.isVip(passengerId)).thenReturn(true);

        // Act
        double fee = baggageFeeCalculator.calculateFee(20.0, 1, passengerId);

        // Assert
        assertEquals(0.0, fee, 0.001);
    }

    @Test
    @DisplayName("Debe cobrar $30 por dos maletas de usuario VIP")
    void debeCobrar30_dosMaletasPeroEsVipTest(){
        // Arrange
        long passengerId = 4L;
        when(passengerService.isVip(passengerId)).thenReturn(true);

        // Act
        double fee = baggageFeeCalculator.calculateFee(15.0, 2, passengerId);

        // Assert
        assertEquals(30.0, fee, 0.001);
    }
    @Test
    @DisplayName("Debe lanzar La excepcion con peso de 0")
    void CuandoPesoEsCero_ValidacionExcepcionTest(){
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> baggageFeeCalculator.calculateFee(0.0, 1, 5L)
        );
        assertEquals("Parametros del equipaje invalidos", ex.getMessage());
    }
}
