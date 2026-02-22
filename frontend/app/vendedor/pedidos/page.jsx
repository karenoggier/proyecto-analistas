"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import styles from "./pedidos.module.css"
import Link from "next/link"
import Image from "next/image"
import VendedorNavbar from "../components/vendedor-navbar"
import LoadingScreen from "../../../components/loading-screen"

export default function VendedorPedidosPage() {
  const router = useRouter()
  const [vendedorProfile, setVendedorProfile] = useState(null)
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  const [showFilters, setShowFilters] = useState(false)
  
  const [selectedTab, setSelectedTab] = useState("todos")
  const [showOrderDetail, setShowOrderDetail] = useState(false)
  const [showRejectModal, setShowRejectModal] = useState(false)
  const [selectedOrder, setSelectedOrder] = useState(null)
  const [rejectReason, setRejectReason] = useState("")
  const [searchQuery, setSearchQuery] = useState("")
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(false)
  const [hasLoadedOnce, setHasLoadedOnce] = useState(false)

  // Filtros de fecha
  const [dateRange, setDateRange] = useState("HOY") 
  const [customStart, setCustomStart] = useState("")
  const [customEnd, setCustomEnd] = useState("")
  // Estados temporales para el modal
  const [tempDateRange, setTempDateRange] = useState("HOY")
  const [tempCustomStart, setTempCustomStart] = useState("")
  const [tempCustomEnd, setTempCustomEnd] = useState("")

  useEffect(() => {
    const token = sessionStorage.getItem("token")
    const rol = sessionStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      router.push("/login")
      return
    }

    const fetchPerfil = async () => {
      try {
        const res = await fetch('/catalogoMs/api/vendedores/perfil', {
          headers: { 'Authorization': `Bearer ${token}` }
        })

        if (res.status === 401 || res.status === 403) {
          sessionStorage.clear()
          window.location.href = "/login?expired=true"
          return
        }

        if (res.ok) {
          const data = await res.json()
          setVendedorProfile(data)
        }
      } catch (error) {
        console.error("Error fetching profile:", error)
      }
    }
    fetchPerfil()
    fetchPedidos()
  }, [router])

  useEffect(() => {
    fetchPedidos()
  }, [selectedTab, dateRange, customStart, customEnd])

  const fetchPedidos = async () => {
    setLoading(true)
    try {
      const token = sessionStorage.getItem("token")
      if (!token) return

      let inicio, fin
      const hoy = new Date()
      const hoyStr = hoy.toISOString().split('T')[0]

      if (dateRange === 'HOY') {
        inicio = hoyStr
        fin = hoyStr
      } else if (dateRange === 'AYER') {
        const ayer = new Date(hoy)
        ayer.setDate(ayer.getDate() - 1)
        inicio = ayer.toISOString().split('T')[0]
        fin = inicio
      } else if (dateRange === 'ULTIMOS_7') {
        const hace7 = new Date(hoy)
        hace7.setDate(hace7.getDate() - 7)
        inicio = hace7.toISOString().split('T')[0]
        fin = hoyStr
      } else if (dateRange === 'PERSONALIZADO') {
        inicio = customStart
        fin = customEnd
      }

      const params = new URLSearchParams()
      if (inicio) params.append("fechaInicio", inicio)
      if (fin) params.append("fechaFin", fin)

      // Mapeo de tabs a estados del backend
      let estadoParam = null
      if (selectedTab === 'pendientes') estadoParam = 'REALIZADO' // Asumimos que REALIZADO es el estado inicial para el vendedor
      else if (selectedTab === 'enpreparacion') estadoParam = 'EN_PREPARACION'
      else if (selectedTab === 'espera') estadoParam = 'EN_ESPERA'
      else if (selectedTab === 'enviaje') estadoParam = 'EN_ENVIO'
      else if (selectedTab === 'entregados') estadoParam = 'ENTREGADO'
      
      if (estadoParam) params.append("estado", estadoParam)

      const res = await fetch(`/pedidoMs/pedidos/vendedor/listado?${params.toString()}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })

      if (res.status === 401 || res.status === 403) {
        sessionStorage.clear()
        window.location.href = "/login?expired=true"
        return
      }

      if (res.ok) {
        const data = await res.json()
        const mappedOrders = data.map(o => ({
          id: o.id,
          numero: o.id.substring(0, 8),
          cliente: (o.nombreCliente || o.apellidoCliente) ? `${o.nombreCliente || ''} ${o.apellidoCliente || ''}`.trim() : 'Cliente',
          fecha: o.fechaCreacion ? new Date(o.fechaCreacion).toLocaleString() : '',
          items: o.detalles ? o.detalles.map(d => ({
            cantidad: `${d.cantidad}x`,
            nombre: d.nombreProducto,
            precio: d.montoUnitario,
            subtotal: d.cantidad * d.montoUnitario,
            imagen: d.imagen
          })) : [],
          observaciones: o.detalles ? o.detalles.map(d => d.observaciones).filter(Boolean).join(', ') : '',
          total: o.montoTotal,
          subtotalProductos: o.montoTotalProductos,
          costoEnvio: o.costoEnvio,
          estado: o.estado ? o.estado.charAt(0) + o.estado.slice(1).toLowerCase().replace('_', ' ') : '',
          rawEstado: o.estado, // Guardamos el estado original para lógica
          telefono: o.telefonoCliente || '',
          direccion: o.metodoEnvio === 'ENVIO_A_DOMICILIO' && o.direccion 
            ? `${o.direccion.calle} ${o.direccion.numero}, ${o.direccion.localidad}` 
            : 'Retiro en local',
          metodoEnvio: o.metodoEnvio,
          motivoRechazo: ''
        }))
        setOrders(mappedOrders)
      }
    } catch (error) {
      console.error("Error fetching orders:", error)
    } finally {
      setLoading(false)
      setHasLoadedOnce(true)
    }
  }

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

  const getStatusClass = (estadoRaw) => {
    if (!estadoRaw) return ""
    const s = estadoRaw.toUpperCase()
    switch (s) {
      case "REALIZADO": // Pendiente para el vendedor
        return styles.statusNuevo
      case "EN_ESPERA":
        return styles.statusEnEspera
      case "EN_PREPARACION":
        return styles.statusEnPrep
      case "EN_ENVIO":
        return styles.statusEnViaje
      case "ENTREGADO":
        return styles.statusEntregado
      case "RECHAZADO":
        return styles.statusRechazado
      default:
        return ""
    }
  }

  const handleViewDetail = (order) => {
    setSelectedOrder(order)
    setShowOrderDetail(true)
  }

  const updateOrderStatus = async (orderId, newStatus) => {
    try {
      const token = sessionStorage.getItem("token")
      const res = await fetch(`/pedidoMs/pedidos/${orderId}/estado`, {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: newStatus, 
      })

      if (res.ok) {
        const mappedStatus = newStatus.charAt(0) + newStatus.slice(1).toLowerCase().replace('_', ' ')
        setOrders((prev) =>
          prev.map((o) => (o.id === orderId ? { ...o, rawEstado: newStatus, estado: mappedStatus } : o)),
        )
        if (selectedOrder && selectedOrder.id === orderId) {
          setSelectedOrder((prev) => ({ ...prev, rawEstado: newStatus, estado: mappedStatus }))
        }
        
        // Cambiar a la pestaña del nuevo estado
        const tabMap = {
          'REALIZADO': 'pendientes',
          'EN_ESPERA': 'espera',
          'EN_PREPARACION': 'enpreparacion',
          'EN_ENVIO': 'enviaje',
          'ENTREGADO': 'entregados',
          'RECHAZADO': 'todos'
        }
        const newTab = tabMap[newStatus] || 'todos'
        setSelectedTab(newTab)
      }
    } catch (error) {
      console.error("Error updating status:", error)
    }
  }

  const handleReject = (order) => {
    setSelectedOrder(order)
    setShowRejectModal(true)
  }

  const handleConfirmReject = () => {
    updateOrderStatus(selectedOrder.id, "RECHAZADO")
    setShowRejectModal(false)
    setRejectReason("")
  }

  const renderActionButtons = (order) => {
    const isDelivery = order.metodoEnvio === 'ENVIO_A_DOMICILIO';

    switch (order.rawEstado) {
      case "REALIZADO":
        return (
          <>
            <button className={styles.orderActionButtonAccept} onClick={() => updateOrderStatus(order.id, "EN_PREPARACION")}>
              Pasar a preparación
            </button>
            {/* <button className={styles.orderActionButtonReject} onClick={() => handleReject(order)}>
              Rechazar pedido
            </button> */}
          </>
        )
      case "EN_PREPARACION":
        return (
          <button className={styles.orderActionButtonAccept} onClick={() => updateOrderStatus(order.id, "EN_ESPERA")}>
            Pasar a listo
          </button>
        )
      case "EN_ESPERA":
        if (isDelivery) {
          return (
            <button className={styles.orderActionButtonAccept} onClick={() => updateOrderStatus(order.id, "EN_ENVIO")}>
              Pasar a en viaje
            </button>
          )
        } else {
          return (
            <button className={styles.orderActionButtonAccept} onClick={() => updateOrderStatus(order.id, "ENTREGADO")}>
              Marcar como entregado
            </button>
          )
        }
      case "EN_ENVIO":
        return (
          <button className={styles.orderActionButtonAccept} onClick={() => updateOrderStatus(order.id, "ENTREGADO")}>
            Marcar como entregado
          </button>
        )
      case "ENTREGADO":
      case "RECHAZADO":
        return null
      default:
        return null
    }
  }

  const filteredOrders = orders.filter((order) => {
    // El filtrado por estado (tab) ya lo hace el backend, excepto para 'todos' que trae todo lo del rango.
    // Aquí filtramos por búsqueda de texto localmente
    if (!searchQuery) return true
    const q = searchQuery.toLowerCase()
    return (
      order.numero?.toLowerCase().includes(q) ||
      order.cliente?.toLowerCase().includes(q) ||
      order.id?.toLowerCase().includes(q) ||
      order.items.some((item) => item.nombre.toLowerCase().includes(q))
    )
  })

  const handleApplyFilters = () => {
    setDateRange(tempDateRange)
    setCustomStart(tempCustomStart)
    setCustomEnd(tempCustomEnd)
    setShowFilters(false)
  }

  const openFilters = () => {
    setTempDateRange(dateRange)
    setTempCustomStart(customStart)
    setTempCustomEnd(customEnd)
    setShowFilters(true)
  }

  const handleLogout = () => {
    sessionStorage.clear()
    router.push("/login")
  }

  const handleNavigate = (path) => {
    window.location.href = path
  }

  if (loading && !hasLoadedOnce) {
    return <LoadingScreen text="Cargando pedidos..." />
  }

  return (
    <div className={styles.pageWrapper}>
      <VendedorNavbar profile={vendedorProfile} />

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
            className={`${styles.tab} ${selectedTab === "espera" ? styles.tabActive : ""}`}
            onClick={() => setSelectedTab("espera")}
          >
            En espera
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
        </div>

        {/* ACTIVE FILTER DISPLAY */}
        <div className={styles.activeFilterDisplay}>
          <span>Mostrando pedidos del:</span>
          <span className={styles.activeFilterBadge}>
            {dateRange === 'PERSONALIZADO' 
              ? `${new Date(customStart).toLocaleDateString()} - ${new Date(customEnd).toLocaleDateString()}`
              : (dateRange === 'HOY' ? 'Hoy' : (dateRange === 'AYER' ? 'Ayer' : 'Últimos 7 días'))
            }
          </span>
        </div>

        {/* SEARCH BAR */}
        <div className={styles.searchBar}>
          <div className={styles.searchInputWrapper}>
            <input
              type="text"
              className={styles.searchInput}
              placeholder="Buscar..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button className={styles.searchIconBtn}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
              </svg>
            </button>
          </div>
          <button className={styles.filterBtn} onClick={openFilters}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="#ff4b7e">
              <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z" />
            </svg>
          </button>
        </div>

        {/* ORDERS LIST */}
        {loading ? (
          <LoadingScreen text="Cargando pedidos..." />
        ) : filteredOrders.length === 0 ? (
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
                    <span className={`${styles.orderStatus} ${getStatusClass(order.rawEstado)}`}>{order.estado}</span>
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
                  <p className={styles.orderTotal}>Total: ${(parseFloat(order.subtotalProductos || 0) + parseFloat(order.costoEnvio || 0)).toFixed(2)}</p>
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
              <span className={`${styles.detailStatus} ${getStatusClass(selectedOrder.rawEstado)}`}>
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
                    <p>
                      <strong>Tipo de entrega:</strong> {selectedOrder.metodoEnvio === 'ENVIO_A_DOMICILIO' ? 'Envío a domicilio' : 'Retiro en local'}
                    </p>
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
                    <p>${item.subtotal}</p>
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
                  <span>${selectedOrder.subtotalProductos}</span>
                </div>
                <div className={styles.detailTotalRow}>
                  <span>Costo de envío:</span>
                  <span>${selectedOrder.costoEnvio}</span>
                </div>
                <div className={styles.detailTotalRowFinal}>
                  <span>Total: </span>
                  <span>${(parseFloat(selectedOrder.subtotalProductos || 0) + parseFloat(selectedOrder.costoEnvio || 0)).toFixed(2)}</span>
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
                  {selectedOrder.rawEstado === "REALIZADO" && (
                    <>
                      <button
                        className={styles.detailActionButtonAccept}
                        onClick={() => {
                          updateOrderStatus(selectedOrder.id, "EN_PREPARACION")
                          setShowOrderDetail(false)
                        }}
                      >
                        Pasar a preparación
                      </button>
                      {/* <button
                        className={styles.detailActionButtonReject}
                        onClick={() => {
                          setShowOrderDetail(false)
                          handleReject(selectedOrder)
                        }}
                      >
                        Rechazar pedido
                      </button> */}
                    </>
                  )}
                  {selectedOrder.rawEstado === "EN_PREPARACION" && (
                    <button
                      className={styles.detailActionButtonAccept}
                      onClick={() => {
                        updateOrderStatus(selectedOrder.id, "EN_ESPERA")
                        setShowOrderDetail(false)
                      }}
                    >
                      Pasar a listo
                    </button>
                  )}
                  {selectedOrder.rawEstado === "EN_ESPERA" && (
                    <button
                      className={styles.detailActionButtonAccept}
                      onClick={() => {
                        const nextStatus = selectedOrder.metodoEnvio === 'ENVIO_A_DOMICILIO' ? "EN_ENVIO" : "ENTREGADO";
                        updateOrderStatus(selectedOrder.id, nextStatus)
                        setShowOrderDetail(false)
                      }}
                    >
                      {selectedOrder.metodoEnvio === 'ENVIO_A_DOMICILIO' ? "Pasar a en viaje" : "Marcar como entregado"}
                    </button>
                  )}
                  {selectedOrder.rawEstado === "EN_ENVIO" && (
                    <button
                      className={styles.detailActionButtonAccept}
                      onClick={() => {
                        updateOrderStatus(selectedOrder.id, "ENTREGADO")
                        setShowOrderDetail(false)
                      }}
                    >
                      Marcar como entregado
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

      {/* FILTERS MODAL */}
      {showFilters && (
        <div className={styles.modalOverlay} onClick={() => setShowFilters(false)}>
          <div className={styles.filtersModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.filtersHeader}>
              <h2>Filtrar pedidos</h2>
              <button className={styles.modalClose} onClick={() => setShowFilters(false)}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18" />
                  <line x1="6" y1="6" x2="18" y2="18" />
                </svg>
              </button>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3>Rango de fechas</h3>
              </div>
              <label className={styles.radioLabel}>
                <input type="radio" className={styles.radioInput} checked={tempDateRange === 'HOY'} onChange={() => setTempDateRange('HOY')} />
                Hoy
              </label>
              <label className={styles.radioLabel}>
                <input type="radio" className={styles.radioInput} checked={tempDateRange === 'AYER'} onChange={() => setTempDateRange('AYER')} />
                Ayer
              </label>
              <label className={styles.radioLabel}>
                <input type="radio" className={styles.radioInput} checked={tempDateRange === 'ULTIMOS_7'} onChange={() => setTempDateRange('ULTIMOS_7')} />
                Últimos 7 días
              </label>
              <label className={styles.radioLabel}>
                <input type="radio" className={styles.radioInput} checked={tempDateRange === 'PERSONALIZADO'} onChange={() => setTempDateRange('PERSONALIZADO')} />
                Personalizado
              </label>

              {tempDateRange === 'PERSONALIZADO' && (
                <div className={styles.dateInputs}>
                  <div className={styles.dateInputGroup}>
                    <span className={styles.dateLabel}>Desde</span>
                    <input 
                      type="date" 
                      className={styles.dateInput} 
                      value={tempCustomStart} 
                      onChange={(e) => setTempCustomStart(e.target.value)} 
                    />
                  </div>
                  <div className={styles.dateInputGroup}>
                    <span className={styles.dateLabel}>Hasta</span>
                    <input 
                      type="date" 
                      className={styles.dateInput} 
                      value={tempCustomEnd} 
                      onChange={(e) => setTempCustomEnd(e.target.value)} 
                    />
                  </div>
                </div>
              )}
            </div>

            <div className={styles.filterActions}>
              <button className={styles.filterCancelBtn} onClick={() => setShowFilters(false)}>Cancelar</button>
              <button className={styles.filterApplyBtn} onClick={handleApplyFilters}>Aplicar filtros</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
