import styles from './ResumenCompra.module.css';

export default function ResumenCompra({ items = 1, subtotal = 10000, envio = 1200, servicio = 500 }) {
  const total = subtotal + envio + servicio;

  return (
    <div className={styles.card}>
      <h3 className={styles.title}>Resumen de compra</h3>
      <div className={styles.row}>
        <span>Productos ({items})</span>
        <span>${subtotal.toLocaleString()}</span>
      </div>
      <div className={styles.row}>
        <span>Costo de envio</span>
        <span>${envio.toLocaleString()}</span>
      </div>
      <div className={styles.row}>
        <span>Tarifa de servicio</span>
        <span>${servicio.toLocaleString()}</span>
      </div>
      <div className={styles.total}>
        <span>Total</span>
        <span>${total.toLocaleString()}</span>
      </div>
    </div>
  );
}
