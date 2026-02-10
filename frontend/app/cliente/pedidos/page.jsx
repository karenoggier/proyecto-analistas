"use client";

import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import styles from "./pedidos.module.css";
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';

const ORDERS = [
  {
    id: "462595752",
    vendor: "Burger King Obelisco",
    logo: "/images/burger-king-logo.jpg",
    status: "En curso",
    statusKey: "enCurso",
    products: 1,
    date: "09/02/2026",
    total: 11700,
  },
  {
    id: "585264864",
    vendor: "McDonald's",
    logo: "/images/mcdonalds-logo.jpg",
    status: "Entregado",
    statusKey: "entregado",
    products: 3,
    date: "01/02/2026",
    total: 35050,
  },
];

const STATUS_FILTERS = ["Entregados", "Cancelados"];
const PERIOD_FILTERS = [
  "Ultima semana",
  "Ultimos 15 dias",
  "Ultimos 30 dias",
  "Ultimos 3 meses",
  "Ultimos 6 meses",
];

export default function MisPedidosPage() {
  const [showFilters, setShowFilters] = useState(false);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [selectedPeriod, setSelectedPeriod] = useState(null);

  const activeFilterCount =
    (selectedStatus ? 1 : 0) + (selectedPeriod ? 1 : 0);

  function handleClear() {
    setSelectedStatus(null);
    setSelectedPeriod(null);
  }

  function handleApply() {
    setShowFilters(false);
  }

  const statusClassName = (key) => {
    if (key === "entregado") return styles.statusEntregado;
    if (key === "cancelado") return styles.statusCancelado;
    return styles.statusEnCurso;
  };

  return (
    <div>
      <Navbar />
    <div className={styles.container}>
      <div className={styles.titleRow}>
        <Link href="/" className={styles.backBtn} aria-label="Volver">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
              fill="#fff"
            />
          </svg>
        </Link>
        <h1 className={styles.title}>Mis Pedidos</h1>
      </div>

      <div className={styles.filterBar}>
        <button
          className={styles.filterBtn}
          onClick={() => setShowFilters(true)}
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z"
              fill="#666"
            />
          </svg>
          Filtrar por...
        </button>
      </div>

      <div className={styles.orderList}>
        {ORDERS.map((order) => (
          <div key={order.id} className={styles.orderCard}>
            <Image
              src={order.logo || "/placeholder.svg"}
              alt={order.vendor}
              width={64}
              height={64}
              className={styles.vendorLogo}
            />
            <div className={styles.orderInfo}>
              <span
                className={`${styles.statusBadge} ${statusClassName(order.statusKey)}`}
              >
                {order.status}
              </span>
              <p className={styles.vendorName}>{order.vendor}</p>
              <p className={styles.orderMeta}>
                {order.products} producto{order.products > 1 ? "s" : ""}
              </p>
              <p className={styles.orderMeta}>
                Fecha de realizacion: {order.date}
              </p>
            </div>
            <div className={styles.orderRight}>
              <p className={styles.orderTotal}>
                ${order.total.toLocaleString("es-AR")}
              </p>
              <Link
                href={`/cliente/detalle-pedido/${order.id}`}
                className={styles.detailLink}
              >
                ver el detalle del pedido {order.id}
              </Link>
            </div>
          </div>
        ))}
      </div>

      {showFilters && (
        <div
          className={styles.modalOverlay}
          onClick={() => setShowFilters(false)}
        >
          <div
            className={styles.modalContent}
            onClick={(e) => e.stopPropagation()}
          >
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>
                Filtros ({activeFilterCount})
              </h2>
              <button
                className={styles.modalCloseBtn}
                onClick={() => setShowFilters(false)}
                aria-label="Cerrar filtros"
              >
                &times;
              </button>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3 className={styles.filterSectionTitle}>Estado</h3>
                <button className={styles.collapseBtn} aria-label="Colapsar">
                  &minus;
                </button>
              </div>
              <ul className={styles.radioList}>
                {STATUS_FILTERS.map((s) => (
                  <li
                    key={s}
                    className={styles.radioItem}
                    onClick={() =>
                      setSelectedStatus(selectedStatus === s ? null : s)
                    }
                  >
                    <span
                      className={`${styles.radioCircle} ${selectedStatus === s ? styles.selected : ""}`}
                    />
                    <span className={styles.radioLabel}>{s}</span>
                  </li>
                ))}
              </ul>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3 className={styles.filterSectionTitle}>Periodo</h3>
                <button className={styles.collapseBtn} aria-label="Colapsar">
                  &minus;
                </button>
              </div>
              <ul className={styles.radioList}>
                {PERIOD_FILTERS.map((p) => (
                  <li
                    key={p}
                    className={styles.radioItem}
                    onClick={() =>
                      setSelectedPeriod(selectedPeriod === p ? null : p)
                    }
                  >
                    <span
                      className={`${styles.radioCircle} ${selectedPeriod === p ? styles.selected : ""}`}
                    />
                    <span className={styles.radioLabel}>{p}</span>
                  </li>
                ))}
              </ul>
            </div>

            <div className={styles.modalActions}>
              <button className={styles.applyBtn} onClick={handleApply}>
                Aplicar
              </button>
              <button className={styles.clearBtn} onClick={handleClear}>
                Limpiar filtros
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
    <Footer />
</div>
  );
}
