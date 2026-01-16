"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import styles from "./perfil.module.css"
import Link from "next/link"
import Image from "next/image"

export default function VendedorPerfilPage() {
  const router = useRouter()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  const [formData, setFormData] = useState({
    nombreNegocio: "",
    telefono: "",
    nombreResponsable: "",
    apellidoResponsable: "",
    horarioApertura: "",
    horarioCierre: "",
    tiempoEspera: "",
    realizaEnvios: "",
    provincia: "",
    localidad: "",
    calle: "",
    numero: "",
    codigoPostal: "",
    notasAdicionales: "",
  })

  useEffect(() => {
    const token = localStorage.getItem("token")
    const rol = localStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      router.push("/login")
    }
  }, [router])

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    console.log("Guardar cambios:", formData)
    // TODO: Implement API call to save profile
  }

  const handleLogout = () => {
    localStorage.clear()
    window.location.href = "/login"
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
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
                        <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
                      </svg>
                      <span>Inicio</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/perfil")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z" />
                      </svg>
                      <span>Mi perfil</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/productos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
                        <path d="M20 4H4v2h16V4zm1 10v-2l-1-5H4l-1 5v2h1v6h10v-6h4v6h2v-6h1zm-9 4H6v-4h6v4z" />
                      </svg>
                      <span>Mis productos</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/pedidos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
                        <path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z" />
                      </svg>
                      <span>Pedidos</span>
                    </button>
                    <button className={styles.userPopoverItem}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
                        <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58.55 0 1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.55-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
                      </svg>
                      <span>Cupones</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={handleLogout}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="#9ca3af">
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
          <svg width="32" height="32" viewBox="0 0 24 24" fill="#ff4b7e">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z" />
          </svg>
          <h1 className={styles.pageTitle}>MI PERFIL</h1>
        </div>

        <div className={styles.formContainer}>
          <h2 className={styles.formTitle}>Información de tu perfil</h2>

          <form onSubmit={handleSubmit}>
            {/* Uploaders */}
            <div className={styles.uploadersRow}>
              <div className={styles.uploadBox}>
                <svg width="40" height="40" viewBox="0 0 24 24" fill="#d1d5db">
                  <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                </svg>
                <p className={styles.uploadLabel}>Logo del negocio</p>
              </div>

              <div className={styles.uploadBox}>
                <svg width="40" height="40" viewBox="0 0 24 24" fill="#d1d5db">
                  <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                </svg>
                <p className={styles.uploadLabel}>Banner de fondo</p>
              </div>
            </div>

            {/* Nombre del negocio */}
            <div className={styles.formGroup}>
              <label className={styles.label}>Nombre del negocio</label>
              <input
                type="text"
                name="nombreNegocio"
                value={formData.nombreNegocio}
                onChange={handleInputChange}
                className={styles.input}
              />
            </div>

            {/* Teléfono */}
            <div className={styles.formGroup}>
              <label className={styles.label}>Teléfono</label>
              <input
                type="tel"
                name="telefono"
                value={formData.telefono}
                onChange={handleInputChange}
                className={styles.input}
              />
            </div>

            {/* Responsable */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Nombre del Responsable</label>
                <input
                  type="text"
                  name="nombreResponsable"
                  value={formData.nombreResponsable}
                  onChange={handleInputChange}
                  className={styles.input}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Apellido del Responsable</label>
                <input
                  type="text"
                  name="apellidoResponsable"
                  value={formData.apellidoResponsable}
                  onChange={handleInputChange}
                  className={styles.input}
                />
              </div>
            </div>

            {/* Horarios */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Horario de apertura</label>
                <input
                  type="time"
                  name="horarioApertura"
                  value={formData.horarioApertura}
                  onChange={handleInputChange}
                  className={styles.input}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Horario de cierre</label>
                <input
                  type="time"
                  name="horarioCierre"
                  value={formData.horarioCierre}
                  onChange={handleInputChange}
                  className={styles.input}
                />
              </div>
            </div>

            {/* Tiempo estimado y Envíos */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Tiempo estimado de espera</label>
                <input
                  type="text"
                  name="tiempoEspera"
                  value={formData.tiempoEspera}
                  onChange={handleInputChange}
                  className={styles.input}
                  placeholder="ej: 30-45 min"
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Realiza envíos</label>
                <select
                  name="realizaEnvios"
                  value={formData.realizaEnvios}
                  onChange={handleInputChange}
                  className={styles.select}
                >
                  <option value="">Seleccione</option>
                  <option value="si">Sí</option>
                  <option value="no">No</option>
                </select>
              </div>
            </div>

            {/* Domicilio del Negocio */}
            <div className={styles.addressSection}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="#ff4b7e">
                <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z" />
              </svg>
              <h3 className={styles.addressTitle}>Domicilio del Negocio</h3>
            </div>

            {/* Provincia y Localidad */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Provincia</label>
                <select
                  name="provincia"
                  value={formData.provincia}
                  onChange={handleInputChange}
                  className={styles.select}
                >
                  <option value="">Seleccione</option>
                  <option value="1">Buenos Aires</option>
                  <option value="2">Córdoba</option>
                  <option value="3">Santa Fe</option>
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Localidad</label>
                <select
                  name="localidad"
                  value={formData.localidad}
                  onChange={handleInputChange}
                  className={styles.select}
                >
                  <option value="">Seleccione</option>
                </select>
              </div>
            </div>

            {/* Calle */}
            <div className={styles.formGroup}>
              <label className={styles.label}>Calle</label>
              <input
                type="text"
                name="calle"
                value={formData.calle}
                onChange={handleInputChange}
                className={styles.input}
                placeholder="Av. Corrientes"
              />
            </div>

            {/* Número y Código Postal */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Número</label>
                <input
                  type="text"
                  name="numero"
                  value={formData.numero}
                  onChange={handleInputChange}
                  className={styles.input}
                  placeholder="1234"
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Código Postal</label>
                <input
                  type="text"
                  name="codigoPostal"
                  value={formData.codigoPostal}
                  onChange={handleInputChange}
                  className={styles.input}
                  placeholder="1428"
                />
              </div>
            </div>

            {/* Notas Adicionales */}
            <div className={styles.formGroup}>
              <label className={styles.label}>Notas Adicionales</label>
              <textarea
                name="notasAdicionales"
                value={formData.notasAdicionales}
                onChange={handleInputChange}
                className={styles.textarea}
                placeholder="Piso, departamento, código de acceso, etc."
                rows="4"
              />
            </div>

            {/* Submit Button */}
            <button type="submit" className={styles.submitButton}>
              Guardar Cambios
            </button>
          </form>
        </div>
      </div>

      {/* FOOTER */}
      <footer className={styles.footer}>
        <div className={`${styles.container} ${styles.footerInner}`}>
          <p className={styles.footerText}>PediloYa © 2026. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  )
}
