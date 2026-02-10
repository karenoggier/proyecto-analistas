"use client";

import { use } from "react";
import Link from "next/link";
import Image from "next/image";
import styles from "./detalle-pedido.module.css";
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

const ORDERS_DATA = {
  "462595752": {
    id: "462595752",
    date: "09/02/2026",
    clientName: "Karen Oggier",
    phone: "3496-511086",
    email: "karenoggier@gmail.com",
    vendor: "Burger King Obelisco",
    total: 11700,
    shippingName: "Karen Oggier",
    shippingAddress: "Santos Vianini 1032, 3081, Buenos Aires, Buenos Aires",
    shippingPhone: "3496511086",
    products: [
      {
        name: "Doble carne Doble queso + Papas medianas",
        qty: 1,
        price: 10000,
        image: "/images/burger-combo.jpg",
        observations: "",
      },
    ],
    subtotal: 10000,
    shipping: 1200,
    serviceFee: 500,
    statusStep: 1, // 0=realizado, 1=en preparacion, 2=espera envio, 3=en envio, 4=entregado
    statusTimestamps: [
      "09/02/2026 - 20:23 hs",
      "09/02/2026 - 20:25 hs",
      null,
      null,
      null,
    ],
  },
  "585264864": {
    id: "585264864",
    date: "01/02/2026",
    clientName: "Karen Oggier",
    phone: "3496-511086",
    email: "karenoggier@gmail.com",
    vendor: "McDonald's",
    total: 35050,
    shippingName: "Karen Oggier",
    shippingAddress: "Santos Vianini 1032, 3081, Buenos Aires, Buenos Aires",
    shippingPhone: "3496511086",
    products: [
      {
        name: "Big Mac Combo",
        qty: 2,
        price: 24000,
        image: "/images/burger-combo.jpg",
        observations: "",
      },
      {
        name: "McFlurry Oreo",
        qty: 1,
        price: 6000,
        image: "/images/burger-combo.jpg",
        observations: "",
      },
    ],
    subtotal: 30000,
    shipping: 3550,
    serviceFee: 1500,
    statusStep: 4,
    statusTimestamps: [
      "01/02/2026 - 19:00 hs",
      "01/02/2026 - 19:10 hs",
      "01/02/2026 - 19:25 hs",
      "01/02/2026 - 19:30 hs",
      "01/02/2026 - 19:55 hs",
    ],
  },
};

const TIMELINE_STEPS = [
  "Pedido realizado",
  "Pedido en preparacion",
  "Pedido en espera de envio",
  "Pedido en envio",
  "Pedido entregado",
];

export default function DetallePedidoPage({ params }) {
  const resolvedParams = use(params);
  const order = ORDERS_DATA[resolvedParams.id] || ORDERS_DATA["462595752"];

  return (
    <div>
      <Navbar />
    <div className={styles.container}>
      <div className={styles.titleRow}>
        <Link
          href="/cliente/mis-pedidos"
          className={styles.backBtn}
          aria-label="Volver"
        >
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
        <h1 className={styles.title}>
          Detalle del Pedido{" "}
          <span className={styles.orderId}>{order.id}</span>
        </h1>
      </div>

      <div className={styles.twoCol}>
        {/* Left Column */}
        <div className={styles.leftCol}>
          {/* Order Info */}
          <div className={styles.card}>
            <h2 className={styles.cardTitle}>Informacion del pedido</h2>
            <div className={styles.infoBox}>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>Numero de pedido:</span>{" "}
                {order.id}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>
                  Fecha de realizacion:
                </span>{" "}
                {order.date}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>
                  Nombre del cliente:
                </span>{" "}
                {order.clientName}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>Telefono:</span>{" "}
                {order.phone}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>Email:</span> {order.email}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>Nombre del local:</span>{" "}
                {order.vendor}
              </p>
              <p className={styles.infoLine}>
                <span className={styles.infoLabel}>Importe total:</span> $
                {order.total.toLocaleString("es-AR")}
              </p>
            </div>
          </div>

          {/* Shipping Info */}
          <div className={styles.card}>
            <h2 className={styles.cardTitle}>Informacion del envio</h2>
            <div className={styles.shippingGrid}>
              <div className={styles.shippingLabel}>Direccion de envio</div>
              <div className={styles.shippingValue}>
                <p className={styles.shippingName}>{order.shippingName}</p>
                <p style={{ margin: 0 }}>{order.shippingAddress}</p>
                <p style={{ margin: 0 }}>{order.shippingPhone}</p>
              </div>
            </div>
          </div>

          {/* Products */}
          <div className={styles.card}>
            <h2 className={styles.cardTitle}>Productos</h2>
            {order.products.map((product, idx) => (
              <div key={idx} className={styles.productRow}>
                <Image
                  src={product.image || "/placeholder.svg"}
                  alt={product.name}
                  width={56}
                  height={56}
                  className={styles.productImage}
                />
                <div className={styles.productInfo}>
                  <p className={styles.productName}>
                    {product.qty} x {product.name}
                  </p>
                  {product.observations && (
                    <p className={styles.productObs}>
                      Observaciones: {product.observations}
                    </p>
                  )}
                  {!product.observations && (
                    <p className={styles.productObs}>Observaciones:</p>
                  )}
                </div>
                <span className={styles.productPrice}>
                  $ {product.price.toLocaleString("es-AR")}
                </span>
              </div>
            ))}
          </div>

          {/* Status Timeline */}
          <div className={styles.timelineCard}>
            <h2 className={styles.timelineTitle}>Estado del pedido</h2>
            <div className={styles.timeline}>
              {TIMELINE_STEPS.map((step, idx) => {
                const isActive = idx <= order.statusStep;
                const isCurrent = idx === order.statusStep;
                const isLast = idx === TIMELINE_STEPS.length - 1;

                return (
                  <div key={idx} className={styles.timelineStep}>
                    <div className={styles.timelineBarWrapper}>
                      <span
                        className={`${styles.timelineDot} ${isActive ? styles.timelineDotActive : ""}`}
                      />
                      {!isLast && (
                        <div
                          className={`${styles.timelineBar} ${
                            isActive && !isCurrent
                              ? styles.timelineBarActive
                              : isCurrent
                                ? styles.timelineBarCurrent
                                : ""
                          }`}
                          style={{ marginLeft: 14 }}
                        />
                      )}
                    </div>
                    <span
                      className={`${styles.timelineStepLabel} ${isActive ? styles.timelineStepLabelActive : ""}`}
                    >
                      {step}
                    </span>
                    {order.statusTimestamps[idx] && (
                      <span className={styles.timelineStepTime}>
                        {order.statusTimestamps[idx]}
                      </span>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        {/* Right Column - Summary */}
        <div className={styles.rightCol}>
          <div className={styles.summaryCard}>
            <h2 className={styles.summaryTitle}>Resumen de compra</h2>
            <div className={styles.summaryRow}>
              <span className={styles.summaryLabel}>
                Productos ({order.products.length})
              </span>
              <span className={styles.summaryValue}>
                ${order.subtotal.toLocaleString("es-AR")}
              </span>
            </div>
            <div className={styles.summaryRow}>
              <span className={styles.summaryLabel}>Costo de envio</span>
              <span className={styles.summaryValue}>
                ${order.shipping.toLocaleString("es-AR")}
              </span>
            </div>
            <div className={styles.summaryRow}>
              <span className={styles.summaryLabel}>Tarifa de servicio</span>
              <span className={styles.summaryValue}>
                ${order.serviceFee.toLocaleString("es-AR")}
              </span>
            </div>
            <hr className={styles.summaryDivider} />
            <div className={`${styles.summaryRow} ${styles.summaryTotal}`}>
              <span className={styles.summaryLabel}>Total</span>
              <span className={styles.summaryValue}>
                ${order.total.toLocaleString("es-AR")}
              </span>
            </div>
            <button className={styles.cancelBtn}>Cancelar pedido</button>
          </div>
        </div>
      </div>
    </div>
    <Footer />
    </div>
  );
}
