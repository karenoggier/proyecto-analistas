'use client';

import { useState } from 'react';
import Link from 'next/link';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './pedidos.module.css';

const orders = [
  {
    id: 1,
    vendor: 'Burger King Obelisco',
    status: 'Entregado',
    date: '05/02/2026',
    total: 11700,
    initial: 'BK',
  },
  {
    id: 2,
    vendor: "McDonald's Lav",
    status: 'Entregado',
    date: '03/02/2026',
    total: 15700,
    initial: 'M',
  },
  {
    id: 3,
    vendor: 'Mostaza Pellegrini',
    status: 'Cancelado',
    date: '01/02/2026',
    total: 8500,
    initial: 'MP',
  },
  {
    id: 4,
    vendor: 'Big Pons - Corrientes',
    status: 'Entregado',
    date: '28/01/2026',
    total: 12300,
    initial: 'BP',
  },
  {
    id: 5,
    vendor: 'Burger King Obelisco',
    status: 'Entregado',
    date: '25/01/2026',
    total: 10000,
    initial: 'BK',
  },
];

const estados = ['Entregados', 'Cancelados'];
const periodos = ['Ultima semana', 'Ultimo mes', 'Ultimos 3 meses', 'Ultimo ano'];

export default function MisPedidosPage() {
  const [showFilter, setShowFilter] = useState(false);
  const [filterEstado, setFilterEstado] = useState([]);
  const [filterPeriodo, setFilterPeriodo] = useState('');

  const toggleEstado = (val) => {
    setFilterEstado(filterEstado.includes(val)
      ? filterEstado.filter((v) => v !== val)
      : [...filterEstado, val]
    );
  };

  const filteredOrders = orders.filter((o) => {
    if (filterEstado.length > 0) {
      const matchMap = { Entregados: 'Entregado', Cancelados: 'Cancelado' };
      const statuses = filterEstado.map((e) => matchMap[e]);
      if (!statuses.includes(o.status)) return false;
    }
    return true;
  });

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>MIS PEDIDOS</h1>
          <button className={styles.filterIconBtn} onClick={() => setShowFilter(true)} aria-label="Filtrar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
              <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3" />
            </svg>
          </button>
        </div>

        <div className={styles.orderList}>
          {filteredOrders.map((order) => (
            <div key={order.id} className={styles.orderCard}>
              <div className={styles.orderLogo}>
                <div className={styles.orderLogoInner}>{order.initial}</div>
              </div>
              <div className={styles.orderInfo}>
                <h3 className={styles.orderVendor}>{order.vendor}</h3>
                <p className={`${styles.orderStatus} ${order.status === 'Cancelado' ? styles.statusCancelled : styles.statusDelivered}`}>
                  {order.status}
                </p>
                <p className={styles.orderMeta}>{order.date} - ${order.total.toLocaleString()}</p>
              </div>
              <Link href="/cliente/detalle-pedido" className={styles.orderDetailLink}>
                ver el detalle
              </Link>
            </div>
          ))}
        </div>
      </main>

      <Footer />

      {/* Filter Modal */}
      {showFilter && (
        <div className={styles.modalOverlay} onClick={() => setShowFilter(false)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2>Filtros</h2>
              <button className={styles.modalClose} onClick={() => setShowFilter(false)}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18" />
                  <line x1="6" y1="6" x2="18" y2="18" />
                </svg>
              </button>
            </div>

            <div className={styles.filterSection}>
              <h3 className={styles.filterSectionTitle}>Estado</h3>
              <div className={styles.filterOptions}>
                {estados.map((e) => (
                  <label key={e} className={styles.checkboxLabel}>
                    <input
                      type="checkbox"
                      checked={filterEstado.includes(e)}
                      onChange={() => toggleEstado(e)}
                      className={styles.checkbox}
                    />
                    {e}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterSection}>
              <h3 className={styles.filterSectionTitle}>Periodo</h3>
              <div className={styles.filterOptions}>
                {periodos.map((p) => (
                  <label key={p} className={styles.radioLabel}>
                    <input
                      type="radio"
                      name="periodo"
                      value={p}
                      checked={filterPeriodo === p}
                      onChange={() => setFilterPeriodo(p)}
                      className={styles.radio}
                    />
                    {p}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterActions}>
              <button className={styles.filterApplyBtn} onClick={() => setShowFilter(false)}>Aplicar</button>
              <button className={styles.filterClearBtn} onClick={() => { setFilterEstado([]); setFilterPeriodo(''); }}>
                Limpiar filtros
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
