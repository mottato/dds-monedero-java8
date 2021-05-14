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
    Movimiento movimiento = new Movimiento(LocalDate.now(),cuanto,true);
    this.agregarMovimiento(movimiento);
  }
/*Code Smell 3 */
  public void sacar(double cuanto) {
    Validaciones.validarMontoNegativo(cuanto);
    Validaciones.validarSaldoMenorAMonto(cuanto, this.getSaldo());
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    Validaciones.validarMaximaExtraccionDiaria(cuanto,limite);
    Movimiento movimiento = new Movimiento(LocalDate.now(),cuanto,false);
    this.agregarMovimiento(movimiento);
  }
  /* Code smell 4: podría recibir un movimiento por parametro y no crear la instancia*/
  public void agregarMovimiento(Movimiento movimiento) {
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
