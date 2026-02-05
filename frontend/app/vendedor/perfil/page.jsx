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
  const [loadingData, setLoadingData] = useState(true)
  const [errors, setErrors] = useState({})

  // Estado para previsualizar imágenes cargadas
  const [previews, setPreviews] = useState({
    logo: null,
    banner: null
  })

  // States for APIs locations
  const [provincias, setProvincias] = useState([])
  const [localidades, setLocalidades] = useState([])
  const [loadingLocalidades, setLoadingLocalidades] = useState(false)
  const [localidadNombre, setLocalidadNombre] = useState([])

  const [formData, setFormData] = useState({
    nombreNegocio: "",
    email: "",
    telefono: "",
    nombreResponsable: "",
    apellidoResponsable: "",
    horarioApertura: "",
    horarioCierre: "",
    tiempoEstimadoEspera: "",
    realizaEnvios: "",
    direccion: {
      provincia: "",
      localidad: "",
      calle: "",
      numero: "",
      codigoPostal: "",
      observaciones: ""
    },
    logo:null,
    banner:null
  })

  // ========= EFFECTS (CARGA DE DATOS) =========
  useEffect(() => {
    const token = localStorage.getItem("token")
    const rol = localStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      window.location.href = "/login"
      return
    }

    const cargarDatos = async () => {
      try {
        // A. Cargar lista de Provincias
        const resProv = await fetch('/usuariosMs/ubicacion/provincias')
        const listaProvincias = await resProv.json()
        setProvincias(listaProvincias)

        // B. Cargar Perfil del Vendedor
        const resPerfil = await fetch('/catalogoMs/api/vendedores/perfil', {
            headers: { 'Authorization': `Bearer ${token}` }
        })

        if (resPerfil.ok) {
            const data = await resPerfil.json()
            
            // --- LÓGICA DE TRADUCCIÓN (TEXTO -> ID) ---
            
            // 1. Buscar ID de Provincia
            const provEncontrada = listaProvincias.find(p => p.nombre === data.direccion?.provincia)
            const idProvincia = provEncontrada ? provEncontrada.id : ""

            // 2. Buscar ID de Localidad 
            let idLocalidad = ""
            if (idProvincia) {
                const resLoc = await fetch(`/usuariosMs/ubicacion/localidades/${idProvincia}`)
                const listaLocalidades = await resLoc.json()
                setLocalidades(listaLocalidades) 

                const locEncontrada = listaLocalidades.find(l => l.nombre === data.direccion?.localidad)
                idLocalidad = locEncontrada ? locEncontrada.id : ""
            }

            // --- LLENADO DEL FORMULARIO ---
            setFormData({
                nombreNegocio: data.nombreNegocio || "",
                email: data.email || "", 
                telefono: data.telefono || "",
                nombreResponsable: data.nombreResponsable || "",
                apellidoResponsable: data.apellidoResponsable || "",
                horarioApertura: data.horarioApertura || "",
                horarioCierre: data.horarioCierre || "",
                tiempoEstimadoEspera: data.tiempoEstimadoEspera || "",
                realizaEnvios: data.realizaEnvios === true ? "si" : (data.realizaEnvios === false ? "no" : ""),
                
                direccion: {
                    provincia: idProvincia, 
                    localidad: idLocalidad, 
                    calle: data.direccion?.calle || "",
                    numero: data.direccion?.numero || "",
                    codigoPostal: data.direccion?.codigoPostal || "",
                    observaciones: data.direccion?.observaciones || ""
                },
                logo: data.logo,
                banner: data.banner
            })

            if(data.logo) setPreviews(prev => ({...prev, logo: data.logo}))
        }
      } catch (error) {
        console.error("Error cargando datos:", error)
      } finally {
        setLoadingData(false)
      }
    }

    cargarDatos()
  }, [])

  
  useEffect(() => {
    const token = localStorage.getItem("token")
    const rol = localStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      router.push("/login")
    }
  }, [router])

  // 2. MANEJADOR PARA CAMPOS SIMPLES (Nivel superior)
  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  // 3. MANEJADOR PARA DIRECCIÓN (Nivel anidado)
  const handleAddressChange = async (e) => {
    const { name, value } = e.target

    setFormData(prev => ({
        ...prev,
        direccion: {
            ...prev.direccion,
            [name]: value,
            // Si cambia provincia, reseteamos localidad
            ...(name === 'provincia' ? { localidad: "" } : {})
        }
    }))

    // Si cambió la provincia, cargar nuevas localidades
    if (name === 'provincia') {
        if (value) {
            setLoadingLocalidades(true)
            try {
                const res = await fetch(`/usuariosMs/ubicacion/localidades/${value}`)
                if (res.ok) setLocalidades(await res.json())
                else setLocalidades([])
            } catch (err) {
                setLocalidades([])
            } finally {
                setLoadingLocalidades(false)
            }
        } else {
            setLocalidades([])
        }
    }
  }

  // 4. MANEJADOR DE IMÁGENES
  const handleImageChange = (e, type) => {
    const file = e.target.files[0]
    if (file) {
      setFormData(prev => ({ ...prev, [type]: file }))
      const objectUrl = URL.createObjectURL(file)
      setPreviews(prev => ({ ...prev, [type]: objectUrl }))
    }
  }

  const fileToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => resolve(reader.result)
      reader.onerror = (error) => reject(error)
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoadingData(true)
    setErrors({})

    try {
      const token = localStorage.getItem("token")
      
      // 1. CONVERTIR IMÁGENES (Solo si son archivos nuevos)
      // Inicializamos con lo que tenga formData (null, URL vieja o Base64 viejo)
      let logoToSend = formData.logo;
      let bannerToSend = formData.banner;

      // Solo si el usuario subió un archivo NUEVO (es tipo File), generamos el nuevo Base64
      if (formData.logo instanceof File) {
        logoToSend = await fileToBase64(formData.logo);
      }

      if (formData.banner instanceof File) {
        bannerToSend = await fileToBase64(formData.banner);
      }

      // 2. ARMAR EL JSON (DTO)
      const vendedorUpdateDTO = {
        nombreNegocio: formData.nombreNegocio,
        telefono: formData.telefono,
        nombreResponsable: formData.nombreResponsable,
        apellidoResponsable: formData.apellidoResponsable,
        horarioApertura: formData.horarioApertura,
        horarioCierre: formData.horarioCierre,
        tiempoEstimadoEspera: formData.tiempoEstimadoEspera,
        realizaEnvios: formData.realizaEnvios === "si", 
        
        // Enviamos las imágenes como STRING Base64
        logo: logoToSend,   
        banner: bannerToSend, 

        direccion: {
          calle: formData.direccion.calle,
          numero: formData.direccion.numero,
          codigoPostal: formData.direccion.codigoPostal,
          observaciones: formData.direccion.observaciones,
          provincia: formData.direccion.provincia, // ID
          localidad: formData.direccion.localidad  // ID
        }
      }

      // 3. ENVIAR COMO JSON 
      const response = await fetch('/catalogoMs/api/vendedores/actualizar', {
        method: 'PUT', 
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(vendedorUpdateDTO)
      })

      if (!response.ok) {
        const errorData = await response.json().catch(() => null);

          if (errorData) {
            // CASO 1: Error de Negocio/Lógica 
            if (errorData.ms_code) {
                if (errorData.ms_code === "GE") {
                    setErrors({ direccion: "No pudimos encontrar esa dirección exacta. Verifica calle y altura." });
                } else {
                    // Error genérico global
                    setErrors({ global: errorData.mensaje || "Error al actualizar el perfil." });
                }
            } 
            // CASO 2: Error de Validación de Campos
            else {
                setErrors(errorData);
            }
          } else {
             setErrors({ global: "Error desconocido en el servidor" });
          }
          return; 
      }

      alert("¡Perfil actualizado correctamente!")

    } catch (error) {
      console.error(error)
      alert("Hubo un error al guardar los cambios: " + error.message)
    } finally {
      setLoadingData(false)
    }
  }

  // Función auxiliar para buscar el nombre de forma segura
  const getNombreLocalidad = () => {
    const idLoc = formData.direccion.localidad;
    if (!idLoc) return ""; 
    const encontrada = localidades.find(l => l.id == idLoc);
    return encontrada ? encontrada.nombre : "Cargando...";
  }

  const handleNavigate = (path) => window.location.href = path
  const handleLogout = () => { localStorage.clear(); window.location.href = "/login" }

  //if (loadingData) return <div className={styles.loading}>Cargando perfil...</div>

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
                <span className={styles.locationValue}>
                  {formData.direccion.calle 
                  ? `${formData.direccion.calle} ${formData.direccion.numero}, ${getNombreLocalidad()}`
                  : "Mi dirección"
                }
                </span>
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
                <span className={styles.userButtonText}>{formData?.nombreNegocio}</span>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                  <path d="M7 10l5 5 5-5z" />
                </svg>
              </button>
              {showUserMenu && (
                <div className={styles.userPopover}>
                  <div className={styles.userPopoverHeader}>
                    <span>{formData?.nombreNegocio}</span>
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
          <h1 className={styles.pageTitle}>MI PERFIL</h1>
        </div>

        <h2 className={styles.formTitle}>Información de tu perfil</h2>
        <div className={styles.formContainer}>
          <form onSubmit={handleSubmit}>
            {/* Uploaders */}
              <div className={styles.uploadersRow}>
                {/* LOGO UPLOAD */}
                <div className={styles.formGroup}>
                  <label className={styles.uploadBox} htmlFor="logo-upload">
                    <input 
                      type="file" 
                      id="logo-upload" 
                      hidden
                      accept="image/*"
                      onChange={(e) => handleImageChange(e, 'logo')}
                    />
                    {previews.logo ? (
                      <img src={previews.logo} alt="Logo preview" className={styles.imagePreview} />
                    ) : (
                      <>
                        <svg width="40" height="40" viewBox="0 0 24 24" fill="#d1d5db">
                          <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                        </svg>
                      </>
                    )}
                  </label>
                  <p className={styles.uploadLabel}>Logo del negocio</p>
                </div>

                {/* BANNER UPLOAD */}
                <div className={styles.formGroup}>
                  <label className={styles.uploadBox} htmlFor="logo-upload">
                    <input 
                      type="file" 
                      id="logo-upload" 
                      hidden
                      accept="image/*"
                      onChange={(e) => handleImageChange(e, 'logo')}
                    />
                    {previews.banner ? (
                      <img src={previews.banner} alt="Banner preview" className={styles.imagePreview} />
                    ) : (
                      <>
                        <svg width="40" height="40" viewBox="0 0 24 24" fill="#d1d5db">
                          <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                        </svg>
                      </>
                    )}
                  </label>
                  <p className={styles.uploadLabel}>Banner de fondo</p>
                </div>
              </div>

            {/* Nombre del negocio */}
            <div className={styles.formGroup}>
              <label className={styles.formLabel}>Nombre del negocio</label>
              <input
                type="text"
                name="nombreNegocio"
                value={formData.nombreNegocio}
                onChange={handleInputChange}
                className={styles.formInput}
              />
              {errors.nombreNegocio && <p className={styles.errorMsg}>{errors.nombreNegocio}</p>}
            </div>
            
            {/* email*/}
            <div className={styles.formGroup}>
              <label className={styles.formLabel}>Email (No editable)</label>
              <input 
                type="email" 
                name="email" 
                value={formData.email} 
                readOnly 
                className={`${styles.formInput} bg-gray-100 text-gray-500 cursor-not-allowed`} 
              />
              {errors.email && <p className={styles.errorMsg}>{errors.email}</p>}
            </div>

            {/* Teléfono */}
            <div className={styles.formGroup}>
              <label className={styles.formLabel}>Teléfono</label>
              <input
                type="tel"
                name="telefono"
                value={formData.telefono}
                onChange={handleInputChange}
                className={styles.formInput}
              />
              {errors.telefono && <p className={styles.errorMsg}>{errors.telefono}</p>}
            </div>

            {/* Responsable */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Nombre del Responsable</label>
                <input
                  type="text"
                  name="nombreResponsable"
                  value={formData.nombreResponsable}
                  onChange={handleInputChange}
                  className={styles.formInput}
                />
                {errors.nombreResponsable && <p className={styles.errorMsg}>{errors.nombreResponsable}</p>}
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Apellido del Responsable</label>
                <input
                  type="text"
                  name="apellidoResponsable"
                  value={formData.apellidoResponsable}
                  onChange={handleInputChange}
                  className={styles.formInput}
                />
                {errors.apellidoResponsable && <p className={styles.errorMsg}>{errors.apellidoResponsable}</p>}
              </div>
            </div>

            {/* Horarios */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Horario de apertura</label>
                <input
                  type="time"
                  name="horarioApertura"
                  value={formData.horarioApertura}
                  onChange={handleInputChange}
                  className={styles.formInput}
                />
                {errors.horarioApertura && <p className={styles.errorMsg}>{errors.horarioApertura}</p>}
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Horario de cierre</label>
                <input
                  type="time"
                  name="horarioCierre"
                  value={formData.horarioCierre}
                  onChange={handleInputChange}
                  className={styles.formInput}
                />
                {errors.horarioCierre && <p className={styles.errorMsg}>{errors.horarioCierre}</p>}
              </div>
            </div>

            {/* Tiempo estimado y Envíos */}
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Tiempo estimado de espera</label>
                <input
                  type="text"
                  name="tiempoEstimadoEspera"
                  value={formData.tiempoEstimadoEspera}
                  onChange={handleInputChange}
                  className={styles.formInput}
                  placeholder="ej: 30-45 min"
                />
                {errors.tiempoEstimadoEspera && <p className={styles.errorMsg}>{errors.tiempoEstimadoEspera}</p>}
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Realiza envíos</label>
                <select
                  name="realizaEnvios"
                  value={formData.realizaEnvios}
                  onChange={handleInputChange}
                  className={styles.formInput}
                >
                  <option value="">Seleccione</option>
                  <option value="si">Sí</option>
                  <option value="no">No</option>
                </select>
              </div>
              {errors.realizaEnvios && <p className={styles.errorMsg}>{errors.realizaEnvios}</p>}
            </div>

            {/* Address Section */}
            <div className={styles.addressSection}>
              <h3 className={styles.addressTitle}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z" />
                </svg>
                Domicilio del Negocio
              </h3>

              <div className={styles.addressGrid}>
                <div className={`${styles.addressRow} ${styles.addressRow2}`}>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Provincia</label>
                    <select
                      name="provincia"
                      value={formData.direccion.provincia}
                      onChange={handleAddressChange}
                      placeholder="Buenos Aires" 
                      className={styles.formInput} 
                      required
                    >
                    <option value="">Seleccione</option>
                    {provincias.map((prov) => (
                            <option key={prov.id} value={prov.id}>{prov.nombre}</option>
                      ))}
                    </select>
                    {errors.provincia && <p className={styles.errorMsg}>{errors.provincia}</p>}
  
                  </div>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Localidad</label>
                    <select 
                      name="localidad"
                      value={formData.direccion.localidad}
                      onChange={handleAddressChange}
                      placeholder="Ciudad Autónoma" 
                      className={styles.formInput} 
                      disabled={!formData.direccion.provincia}
                      required
                    >
                      <option value="">{loadingLocalidades ? "Cargando..." : "Seleccione"}</option>
                        {localidades.map((loc) => (
                            <option key={loc.id} value={loc.id}>{loc.nombre}</option>
                        ))}
                    </select>
                    {errors.localidad && <p className={styles.errorMsg}>{errors.localidad}</p>}
                  </div>
                </div>

                <div className={styles.formGroup}>
                  <label className={styles.formLabel}>Calle</label>
                  <input 
                    type="text" 
                    name="calle"
                    value={formData.direccion.calle}
                    onChange={handleAddressChange}
                    placeholder="Avenida Corrientes" 
                    className={styles.formInput} 
                    required
                  />
                  {errors.calle && <p className={styles.errorMsg}>{errors.calle}</p>}
                </div>

                <div className={styles.formRow}>
                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Numero</label>
                    <input 
                      type="text" 
                      name="numero"
                      value={formData.direccion.numero}
                      onChange={handleAddressChange}
                      placeholder="1234" 
                      className={styles.formInput} 
                      required
                    />
                    {errors.numero && <p className={styles.errorMsg}>{errors.numero}</p>}
                  </div>

                  <div className={styles.formGroup}>
                    <label className={styles.formLabel}>Código Postal</label>
                    <input 
                      type="text" 
                      name="codigoPostal"
                      value={formData.direccion.codigoPostal}
                      onChange={handleAddressChange}
                      placeholder="1425" 
                      className={styles.formInput} 
                      required
                    />
                    {errors.codigoPostal && <p className={styles.errorMsg}>{errors.codigoPostal}</p>}
                  </div>
                </div>

                <div className={styles.formGroup}>
                  <label className={styles.formLabel}>Notas Adicionales</label>
                  <textarea 
                    name="observaciones"
                    value={formData.direccion.observaciones}
                    onChange={handleAddressChange}
                    placeholder="Piso, departamento, código de acceso, etc." 
                    className={styles.formInput} 
                  />
                  {errors.observaciones && <p className={styles.errorMsg}>{errors.observaciones}</p>}
                </div>
              </div>
            </div>

            {errors.global && (
                <div className={styles.errorMessage}>
                  <span>{errors.global}</span>
                </div>
            )}

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
