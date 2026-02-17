import styles from './ResumenCompra.module.css';

const SERVICIO = 500;

export default function ResumenCompra({ items = 1, subtotal = 10000, realizaEnvios = false }) {
  const total = subtotal + SERVICIO;

  return (
    <div className={styles.card}>
      <h3 className={styles.title}>Resumen de compra</h3>
      <div className={styles.row}>
        <span>Productos ({items})</span>
        <span>${subtotal.toLocaleString()}</span>
      </div>
      {realizaEnvios && (
        <div className={styles.row}>
          <span style={{display: 'flex', alignItems: 'center', gap: '5px'}}>
            Costo de envio
            <div className={styles.tooltipWrapper}>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="12" cy="12" r="10" />
                <line x1="12" y1="8" x2="12" y2="12" />
                <line x1="12" y1="16" x2="12.01" y2="16" />
              </svg>
              <span className={styles.tooltip}>El costo de envío será calculado en la sección de dirección.</span>
            </div>
          </span>
          <span>-</span>
        </div>
      )}
      <div className={styles.row}>
        <span>Tarifa de servicio</span>
        <span>${SERVICIO.toLocaleString()}</span>
      </div>
      <div className={styles.total}>
        <span>Total</span>
        <span>${total.toLocaleString()}</span>
      </div>
    </div>
  );
}
