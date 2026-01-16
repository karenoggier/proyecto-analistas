"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import styles from "./pedidos.module.css"
import Link from "next/link"
import Image from "next/image"

export default function VendedorPedidosPage() {
  const router = useRouter()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  const [selectedTab, setSelectedTab] = useState("todos")
  const [showOrderDetail, setShowOrderDetail] = useState(false)
  const [showRejectModal, setShowRejectModal] = useState(false)
  const [selectedOrder, setSelectedOrder] = useState(null)
  const [rejectReason, setRejectReason] = useState("")
  const [searchQuery, setSearchQuery] = useState("")

  const [orders, setOrders] = useState([
    {
      id: 1,
      numero: "Nro de pedido",
      cliente: "Nombre del cliente",
      fecha: "15/01/2026 - 14:30 hs",
      items: [
        { cantidad: "2x", nombre: "McPollo + Papas pequeñas", precio: 7000 },
        { cantidad: "1x", nombre: "McFlurry Oreo", precio: 4000 },
      ],
      observaciones: "Sin pepinillos",
      total: 18000,
      estado: "Nuevo",
      telefono: "+54 9 3496-511088",
      direccion: "Av. Corrientes 1234, CABA",
      motivoRechazo: "",
    },
    {
      id: 2,
      numero: "Nro de pedido",
      cliente: "Nombre del cliente",
      fecha: "15/01/2026 - 13:55 hs",
      items: [{ cantidad: "1x", nombre: "Doble Carne Doble Queso", precio: 10000 }],
      observaciones: "",
      total: 10000,
      estado: "En prep",
      telefono: "+54 9 3496-511088",
      direccion: "Av. Corrientes 1234, CABA",
      motivoRechazo: "",
    },
    {
      id: 3,
      numero: "Nro de pedido",
      cliente: "Nombre del cliente",
      fecha: "15/01/2026 - 13:45 hs",
      items: [
        { cantidad: "2x", nombre: "McPollo + Papas pequeñas", precio: 7000 },
        { cantidad: "1x", nombre: "McFlurry Oreo", precio: 4000 },
      ],
      observaciones: "",
      total: 18000,
      estado: "En viaje",
      telefono: "+54 9 3496-511088",
      direccion: "Av. Corrientes 1234, CABA",
      motivoRechazo: "",
    },
    {
      id: 4,
      numero: "Nro de pedido",
      cliente: "Nombre del cliente",
      fecha: "15/01/2026 - 13:21 hs",
      items: [
        { cantidad: "2x", nombre: "McPollo + Papas pequeñas", precio: 7000 },
        { cantidad: "1x", nombre: "McFlurry Oreo", precio: 4000 },
      ],
      observaciones: "Sin pepinillos",
      total: 18000,
      estado: "Entregado",
      telefono: "+54 9 3496-511088",
      direccion: "Av. Corrientes 1234, CABA",
      motivoRechazo: "",
    },
    {
      id: 5,
      numero: "Nro de pedido",
      cliente: "Nombre del cliente",
      fecha: "15/01/2026 - 13:00 hs",
      items: [{ cantidad: "1x", nombre: "Doble Carne Doble Queso", precio: 10000 }],
      observaciones: "",
      total: 10000,
      estado: "Rechazado",
      telefono: "+54 9 3496-511088",
      direccion: "Av. Corrientes 1234, CABA",
      motivoRechazo: "No tenemos más stock",
    },
  ])

  useEffect(() => {
    const token = localStorage.getItem("token")
    const rol = localStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      router.push("/login")
    }
  }, [router])

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (!e.target.closest(`.${styles.navbarIconWrapper}`)) {
        setShowNotifications(false)
        setShowUserMenu(false)
      }
    }
    document.addEventListener("click", handleClickOutside)
    return () => document.removeEventListener("click", handleClickOutside)
  }, [])

  const getStatusClass = (estado) => {
    switch (estado.toLowerCase()) {
      case "nuevo":
        return styles.statusNuevo
      case "en prep":
        return styles.statusEnPrep
      case "en viaje":
        return styles.statusEnViaje
      case "entregado":
        return styles.statusEntregado
      case "rechazado":
        return styles.statusRechazado
      default:
        return ""
    }
  }

  const handleViewDetail = (order) => {
    setSelectedOrder(order)
    setShowOrderDetail(true)
  }

  const handleReject = (order) => {
    setSelectedOrder(order)
    setShowRejectModal(true)
  }

  const handleConfirmReject = () => {
    setOrders(
      orders.map((o) => (o.id === selectedOrder.id ? { ...o, estado: "Rechazado", motivoRechazo: rejectReason } : o)),
    )
    setShowRejectModal(false)
    setRejectReason("")
  }

  const handleAcceptOrder = (order) => {
    setOrders(orders.map((o) => (o.id === order.id ? { ...o, estado: "En prep" } : o)))
  }

  const handlePrepareOrder = (order) => {
    setOrders(orders.map((o) => (o.id === order.id ? { ...o, estado: "En prep" } : o)))
  }

  const handleMarkReady = (order) => {
    setOrders(orders.map((o) => (o.id === order.id ? { ...o, estado: "En viaje" } : o)))
  }

  const handleConfirmDelivery = (order) => {
    setOrders(orders.map((o) => (o.id === order.id ? { ...o, estado: "Entregado" } : o)))
  }

  const renderActionButtons = (order) => {
    switch (order.estado) {
      case "Nuevo":
        return (
          <>
            <button className={styles.orderActionButtonAccept} onClick={() => handleAcceptOrder(order)}>
              Aceptar pedido
            </button>
            <button className={styles.orderActionButtonReject} onClick={() => handleReject(order)}>
              Rechazar pedido
            </button>
          </>
        )
      case "En prep":
        return (
          <button className={styles.orderActionButtonAccept} onClick={() => handleMarkReady(order)}>
            Marcar listo
          </button>
        )
      case "En viaje":
        return (
          <button className={styles.orderActionButtonAccept} onClick={() => handleConfirmDelivery(order)}>
            Confirmar entrega
          </button>
        )
      case "Entregado":
      case "Rechazado":
        return null
      default:
        return null
    }
  }

  const filteredOrders = orders.filter((order) => {
    if (selectedTab === "todos") return true
    if (selectedTab === "pendientes") return order.estado === "Nuevo"
    if (selectedTab === "enpreparacion") return order.estado === "En prep"
    if (selectedTab === "enviaje") return order.estado === "En viaje"
    if (selectedTab === "entregados") return order.estado === "Entregado"
    if (selectedTab === "rechazados") return order.estado === "Rechazado"
    return true
  })

  const handleLogout = () => {
    localStorage.clear()
    router.push("/login")
  }

  const handleNavigate = (path) => {
    window.location.href = path
  }

  return (
    <div className={styles.pageWrapper}>
      {/* NAVBAR */}
      <nav className={styles.navbar}>
        <div className={styles.navbarInner}>
          <div className={styles.navbarLeft}>
            <Link href="/vendedor" className={styles.logo}>
              <Image src="/logo.png" alt="PediloYa Logo" width={50} height={60} className={styles.logo} priority />
              <span className={styles.logoText}>PediloYa</span>
            </Link>
            <div className={styles.location}>
              <Image src="/pin-de-ubicacion.png" alt="Pin de ubicación" width={30} height={40} />
              <div className={styles.locationText}>
                <span className={styles.locationLabel}>Ubicación</span>
                <span className={styles.locationValue}>Mi dirección</span>
              </div>
            </div>
          </div>

          <div className={styles.navbarRight}>
            <div className={styles.navbarIconWrapper}>
              <button
                className={styles.navbarIcon}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowNotifications(!showNotifications)
                  setShowUserMenu(false)
                }}
              >
                <Image src="/campana-de-notificacion.png" alt="Notificaciones" width={28} height={38} />
              </button>
              {showNotifications && (
                <div className={styles.notificationPopover}>
                  <div className={styles.notificationHeader}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="#9ca3af">
                      <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z" />
                    </svg>
                    <span>Notificaciones</span>
                  </div>
                  <div className={styles.notificationContent}>
                    <p>No tenés notificaciones</p>
                  </div>
                </div>
              )}
            </div>

            <div className={styles.navbarIconWrapper}>
              <button
                className={styles.userButton}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowUserMenu(!showUserMenu)
                  setShowNotifications(false)
                }}
              >
                <Image src="/perfil.png" alt="Foto de perfil" width={35} height={45} />
                <span className={styles.userButtonText}>Local</span>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                  <path d="M7 10l5 5 5-5z" />
                </svg>
              </button>
              {showUserMenu && (
                <div className={styles.userPopover}>
                  <div className={styles.userPopoverHeader}>
                    <span>Local</span>
                  </div>
                  <div className={styles.userPopoverMenu}>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
                      </svg>
                      <span>Inicio</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/perfil")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z" />
                      </svg>
                      <span>Mi perfil</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/productos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M20 4H4v2h16V4zm1 10v-2l-1-5H4l-1 5v2h1v6h10v-6h4v6h2v-6h1zm-9 4H6v-4h6v4z" />
                      </svg>
                      <span>Mis productos</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/pedidos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z" />
                      </svg>
                      <span>Pedidos</span>
                    </button>
                    <button className={styles.userPopoverItem}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58.55 0 1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.55-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
                      </svg>
                      <span>Cupones</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={handleLogout}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
                      </svg>
                      <span>Salir</span>
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* CONTENT */}
      <div className={styles.content}>
        <div className={styles.header}>
          <button className={styles.carouselButton} onClick={() => router.back()}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
            </svg>
          </button>
          <h1 className={styles.pageTitle}>PEDIDOS</h1>
        </div>

        {/* TABS */}
        <div className={styles.tabs}>
          <button
            className={`${styles.tab} ${selectedTab === "todos" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("todos")}
          >
            Todos
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "nuevos" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("nuevos")}
          >
            Nuevos
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "pendientes" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("pendientes")}
          >
            Pendientes
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "enpreparacion" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("enpreparacion")}
          >
            En preparación
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "enviaje" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("enviaje")}
          >
            En viaje
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "entregados" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("entregados")}
          >
            Entregados
          </button>
          <button
            className={`${styles.tab} ${selectedTab === "rechazados" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("rechazados")}
          >
            Rechazados
          </button>
        </div>

        {/* SEARCH BAR */}
        <div className={styles.searchBar}>
          <div className={styles.searchInputWrapper}>
            <input
              type="text"
              className={styles.searchInput}
              placeholder=""
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button className={styles.searchIconBtn}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
              </svg>
            </button>
          </div>
          <button className={styles.filterBtn}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="#ff4b7e">
              <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z" />
            </svg>
          </button>
        </div>

        {/* ORDERS LIST */}
        {filteredOrders.length === 0 ? (
          <div className={styles.emptyState}>
            <p className={styles.emptyText}>No hay pedidos</p>
          </div>
        ) : (
          <div className={styles.ordersList}>
            {filteredOrders.map((order) => (
              <div key={order.id} className={styles.orderCard}>
                <div className={styles.orderHeader}>
                  <div className={styles.orderInfo}>
                    <h3 className={styles.orderNumber}>
                      #{order.numero} - {order.cliente}
                    </h3>
                  </div>
                  <div className={styles.orderMeta}>
                    <span className={styles.orderDate}>{order.fecha}</span>
                    <span className={`${styles.orderStatus} ${getStatusClass(order.estado)}`}>{order.estado}</span>
                  </div>
                </div>

                <div className={styles.orderItems}>
                  {order.items.map((item, idx) => (
                    <p key={idx} className={styles.orderItem}>
                      <strong>{item.cantidad}</strong> {item.nombre}
                    </p>
                  ))}
                  {order.observaciones && (
                    <p className={styles.orderObservations}>
                      <strong>Obs:</strong> {order.observaciones}
                    </p>
                  )}
                </div>

                <div className={styles.orderFooter}>
                  <p className={styles.orderTotal}>Total: ${order.total}</p>
                  <div className={styles.orderActions}>
                    <button className={styles.orderActionButton} onClick={() => handleViewDetail(order)}>
                      Ver detalle
                    </button>
                    {renderActionButtons(order)}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* FOOTER */}
      <footer className={styles.footer}>
        <div className={`${styles.container} ${styles.footerInner}`}>
          <p className={styles.footerText}>PediloYa © 2026. Todos los derechos reservados.</p>
        </div>
      </footer>

      {/* ORDER DETAIL MODAL */}
      {showOrderDetail && selectedOrder && (
        <div className={styles.modalOverlay} onClick={() => setShowOrderDetail(false)}>
          <div className={styles.detailModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.detailHeader}>
              <h2 className={styles.detailTitle}>Detalle del Pedido #{selectedOrder.numero}</h2>
              <button className={styles.modalClose} onClick={() => setShowOrderDetail(false)}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className={styles.detailBody}>
              <span className={`${styles.detailStatus} ${getStatusClass(selectedOrder.estado)}`}>
                {selectedOrder.estado}
              </span>

              <div className={styles.detailSection}>
                <h3 className={styles.detailSectionTitle}>Datos del cliente y entrega</h3>
                <div className={styles.detailInfo}>
                  <div className={styles.detailInfoText}>
                    <p>
                      <strong>Cliente:</strong> {selectedOrder.cliente}
                    </p>
                    <p>
                      <strong>Teléfono:</strong> {selectedOrder.telefono}
                    </p>
                    <p>
                      <strong>Dirección:</strong> {selectedOrder.direccion}
                    </p>
                  </div>
                  <div className={styles.detailMap}>
                    <div className={styles.mapPlaceholder}>
                      <svg width="40" height="40" viewBox="0 0 24 24" fill="#ef4444">
                        <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z" />
                      </svg>
                    </div>
                  </div>
                </div>
              </div>

              <div className={styles.detailSection}>
                <h3 className={styles.detailSectionTitle}>Detalle de ítems</h3>
                {selectedOrder.items.map((item, idx) => (
                  <div key={idx} className={styles.detailItem}>
                    <p>
                      {item.cantidad} {item.nombre}
                    </p>
                    <p>${item.precio} c/u</p>
                  </div>
                ))}
              </div>

              <div className={styles.detailSection}>
                <h3 className={styles.detailSectionTitle}>Observaciones</h3>
                <p className={styles.detailObservations}>{selectedOrder.observaciones || "Sin observaciones"}</p>
              </div>

              <div className={styles.detailTotals}>
                <div className={styles.detailTotalRow}>
                  <span>Subtotal:</span>
                  <span>${selectedOrder.total}</span>
                </div>
                <div className={styles.detailTotalRow}>
                  <span>Costo de envío:</span>
                  <span>Gratis</span>
                </div>
                <div className={styles.detailTotalRowFinal}>
                  <span>Total: ${selectedOrder.total}</span>
                </div>
              </div>

              {selectedOrder.estado === "Rechazado" && selectedOrder.motivoRechazo && (
                <div className={styles.detailRejectSection}>
                  <label className={styles.detailRejectLabel}>Motivo de rechazo</label>
                  <div className={styles.detailRejectDisplay}>{selectedOrder.motivoRechazo}</div>
                </div>
              )}

              {selectedOrder.estado !== "Entregado" && selectedOrder.estado !== "Rechazado" && (
                <div className={styles.detailActions}>
                  <button className={styles.detailActionButton} onClick={() => setShowOrderDetail(false)}>
                    Cerrar
                  </button>
                  {selectedOrder.estado === "Nuevo" && (
                    <>
                      <button
                        className={styles.detailActionButtonAccept}
                        onClick={() => {
                          handleAcceptOrder(selectedOrder)
                          setShowOrderDetail(false)
                        }}
                      >
                        Aceptar pedido
                      </button>
                      <button
                        className={styles.detailActionButtonReject}
                        onClick={() => {
                          setShowOrderDetail(false)
                          handleReject(selectedOrder)
                        }}
                      >
                        Rechazar pedido
                      </button>
                    </>
                  )}
                  {selectedOrder.estado === "En prep" && (
                    <button
                      className={styles.detailActionButtonAccept}
                      onClick={() => {
                        handleMarkReady(selectedOrder)
                        setShowOrderDetail(false)
                      }}
                    >
                      Marcar listo
                    </button>
                  )}
                  {selectedOrder.estado === "En viaje" && (
                    <button
                      className={styles.detailActionButtonAccept}
                      onClick={() => {
                        handleConfirmDelivery(selectedOrder)
                        setShowOrderDetail(false)
                      }}
                    >
                      Confirmar entrega
                    </button>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      )}

      {/* REJECT MODAL */}
      {showRejectModal && selectedOrder && (
        <div className={styles.modalOverlay} onClick={() => setShowRejectModal(false)}>
          <div className={styles.rejectModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.rejectHeader}>
              <h2 className={styles.rejectTitle}>Rechazar pedido #{selectedOrder.numero}</h2>
              <button className={styles.modalClose} onClick={() => setShowRejectModal(false)}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className={styles.rejectBody}>
              <label className={styles.rejectLabel}>Motivo de rechazo</label>
              <textarea
                className={styles.rejectInput}
                placeholder=""
                value={rejectReason}
                onChange={(e) => setRejectReason(e.target.value)}
                rows={4}
              />
              <button className={styles.rejectButton} onClick={handleConfirmReject}>
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
