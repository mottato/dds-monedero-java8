package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }
/* Code Smell 3: podría usarse el metodo agregarMovimiento*/
  public void poner(double cuanto) {
    Validaciones.validarMontoNegativo(cuanto);
    Validaciones.validarMaximaCantidadDeDepositos(this);
    this.agregarMovimiento(LocalDate.now(),cuanto,true);
  }
/*Code Smell 3 */
  public void sacar(double cuanto) {
    Validaciones.validarMontoNegativo(cuanto);
    Validaciones.validarSaldoMenorAMonto(cuanto, this.getSaldo());
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    Validaciones.validarMaximaExtraccionDiaria(cuanto,limite);
    this.agregarMovimiento(LocalDate.now(),cuanto,false);
  }
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
