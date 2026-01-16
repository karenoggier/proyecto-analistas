"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import styles from "./productos.module.css"
import Link from "next/link"
import Image from "next/image"

export default function VendedorProductosPage() {
  const router = useRouter()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  const [showFilters, setShowFilters] = useState(false)
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedProduct, setSelectedProduct] = useState(null)
  const [isEditing, setIsEditing] = useState(false)
  const [isNewProduct, setIsNewProduct] = useState(false)

  // Estado para previsualizar imágenes cargadas
  const [previews, setPreviews] = useState({
    logo: null,
    banner: null
  })

  const [products, setProducts] = useState([
    {
      id: 1,
      nombre: "Doble carne Doble queso + Papas medianas",
      descripcion:
        "Hamburguesa doble carne junior 100% vacuno, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompañada de papas medianas.",
      precio: 10000,
      imagen: "/double-cheeseburger-with-fries.jpg",
      categoria: "Comida",
      subcategoria: "Hamburguesa",
      disponible: true,
      observaciones: "",
    },
    {
      id: 2,
      nombre: "Doble carne Doble queso + Papas medianas",
      descripcion:
        "Hamburguesa doble carne junior 100% vacuno, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompañada de papas medianas.",
      precio: 10000,
      imagen: "/double-cheeseburger-with-fries.jpg",
      categoria: "Comida",
      subcategoria: "Hamburguesa",
      disponible: true,
      observaciones: "",
    },
    {
      id: 3,
      nombre: "McPollo + Papas Pequeñas",
      descripcion: "Medallón de pollo frito, mayonesa y lechuga con papas pequeñas.",
      precio: 7000,
      imagen: "/chicken-burger-with-small-fries.jpg",
      categoria: "Comida",
      subcategoria: "Hamburguesa",
      disponible: true,
      observaciones: "",
    },
    {
      id: 4,
      nombre: "McPollo + Papas Pequeñas",
      descripcion: "Medallón de pollo frito, mayonesa y lechuga con papas pequeñas.",
      precio: 7000,
      imagen: "/chicken-burger-with-small-fries.jpg",
      categoria: "Comida",
      subcategoria: "Hamburguesa",
      disponible: true,
      observaciones: "",
    },
    {
      id: 5,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 6,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 7,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 8,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 9,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 10,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 11,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
    {
      id: 12,
      nombre: "McFlurry Oreo",
      descripcion: "Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.",
      precio: 4000,
      imagen: "/mcflurry-oreo-ice-cream.jpg",
      categoria: "Comida",
      subcategoria: "Postre",
      disponible: true,
      observaciones: "",
    },
  ])

  const [formData, setFormData] = useState({
    nombre: "",
    descripcion: "",
    precio: "",
    categoria: "",
    subcategoria: "",
    disponible: "",
    observaciones: "",
    imagen: "",
  })

  const [filters, setFilters] = useState({
    categorias: [],
    subcategorias: [],
    disponibilidad: [],
  })

  const subcategoriasList = [
    "Hamburguesa",
    "Ensalada",
    "Gaseoso",
    "Pizza",
    "Sushi",
    "Agua",
    "Empanada",
    "Helado",
    "Cerveza",
    "Milanesa",
    "Postre",
    "Vino",
    "Pasta",
    "Calzaco",
    "Trago",
    "Parrilla",
    "Vegetariano",
    "Café",
  ]

  const filterCount = filters.categorias.length + filters.subcategorias.length + filters.disponibilidad.length

  useEffect(() => {
    const token = localStorage.getItem("token")
    const rol = localStorage.getItem("rol")

    if (!token || rol !== "VENDEDOR") {
      router.push("/login")
    }
  }, [router])

  const handleProductClick = (product) => {
    setSelectedProduct(product)
    setFormData({
      nombre: product.nombre,
      descripcion: product.descripcion,
      precio: product.precio.toString(),
      categoria: product.categoria,
      subcategoria: product.subcategoria,
      disponible: product.disponible ? "Disponible" : "No disponible",
      observaciones: product.observaciones || "",
      imagen: product.imagen,
    })
    setIsEditing(false)
    setIsNewProduct(false)
  }

  const handleNuevoClick = () => {
    setSelectedProduct(null)
    setFormData({
      nombre: "",
      descripcion: "",
      precio: "",
      categoria: "",
      subcategoria: "",
      disponible: "",
      observaciones: "",
      imagen: "",
    })
    setIsEditing(true)
    setIsNewProduct(true)
  }

  const handleEditarClick = () => {
    if (selectedProduct) {
      setIsEditing(true)
      setIsNewProduct(false)
    }
  }

  const handleEliminarClick = () => {
    if (selectedProduct) {
      setProducts(products.filter((p) => p.id !== selectedProduct.id))
      setSelectedProduct(null)
      setFormData({
        nombre: "",
        descripcion: "",
        precio: "",
        categoria: "",
        subcategoria: "",
        disponible: "",
        observaciones: "",
        imagen: "",
      })
    }
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (isNewProduct) {
      const newProduct = {
        id: Date.now(),
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        precio: Number.parseInt(formData.precio),
        categoria: formData.categoria,
        subcategoria: formData.subcategoria,
        disponible: formData.disponible === "Disponible",
        observaciones: formData.observaciones,
        imagen: formData.imagen || "/mcflurry-oreo-ice-cream.jpg",
      }
      setProducts([...products, newProduct])
    } else if (selectedProduct) {
      setProducts(
        products.map((p) =>
          p.id === selectedProduct.id
            ? {
                ...p,
                nombre: formData.nombre,
                descripcion: formData.descripcion,
                precio: Number.parseInt(formData.precio),
                categoria: formData.categoria,
                subcategoria: formData.subcategoria,
                disponible: formData.disponible === "Disponible",
                observaciones: formData.observaciones,
              }
            : p,
        ),
      )
    }
    setIsEditing(false)
    setIsNewProduct(false)
  }

  const handleFilterChange = (type, value) => {
    setFilters((prev) => {
      const current = prev[type]
      if (current.includes(value)) {
        return { ...prev, [type]: current.filter((v) => v !== value) }
      } else {
        return { ...prev, [type]: [...current, value] }
      }
    })
  }

  const clearFilters = () => {
    setFilters({
      categorias: [],
      subcategorias: [],
      disponibilidad: [],
    })
  }

  const handleLogout = () => {
    localStorage.clear()
    window.location.href = "/login"
  }

  const handleNavigate = (path) => {
    window.location.href = path
  }

  // Lógica para cargar imágenes y mostrar previsualización
  const handleImageChange = (e, type) => {
    const file = e.target.files[0];
    if (file) {
      // 1. Guardar el archivo en el form data
      setFormData(prev => ({ ...prev, [type]: file }));
      
      // 2. Crear URL para previsualización
      const objectUrl = URL.createObjectURL(file);
      setPreviews(prev => ({ ...prev, [type]: objectUrl }));
    }
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

      {/* MAIN CONTENT */}
      <main className={styles.mainContent}>
        {/* LEFT SIDE - Products */}
        <div className={styles.leftPanel}>
          {/* Header */}
          <div className={styles.header}>
            <button className={styles.carouselButton} onClick={() => router.back()}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
              </svg>
            </button>
            <h1 className={styles.pageTitle}>MIS PRODUCTOS</h1>
          </div>

          {/* Search and Filter Bar */}
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
            <button className={styles.filterBtn} onClick={() => setShowFilters(true)}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="#ff4b7e">
                <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z" />
              </svg>
            </button>
          </div>

          {/* Products Grid */}
          <div className={styles.productsGrid}>
            {products.map((product) => (
              <div
                key={product.id}
                className={`${styles.productCard} ${selectedProduct?.id === product.id ? styles.productCardSelected : ""}`}
                onClick={() => handleProductClick(product)}
              >
                <div className={styles.productCardContent}>
                  <div className={styles.productInfo}>
                    <h3 className={styles.productName}>{product.nombre}</h3>
                    <p className={styles.productDescription}>{product.descripcion}</p>
                    <p className={styles.productPrice}>$ {product.precio}</p>
                  </div>
                  <div className={styles.productImage}>
                    <img src={product.imagen || "/placeholder.svg"} alt={product.nombre} />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* RIGHT SIDE - Form Panel */}
        <div className={styles.rightPanel}>
          {/* Action Buttons */}
          <div className={styles.actionButtons}>
            <button className={styles.actionBtn} onClick={handleNuevoClick}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm5 11h-4v4h-2v-4H7v-2h4V7h2v4h4v2z" />
              </svg>
              Nuevo
            </button>
            <button className={styles.actionBtn} onClick={handleEditarClick}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z" />
              </svg>
              Editar
            </button>
            <button className={styles.actionBtn} onClick={handleEliminarClick}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z" />
              </svg>
              Eliminar
            </button>
          </div>

          {/* Form */}
          <div className={styles.formPanel}>
            <div className={styles.formImageUpload}>
              <label className={styles.imageUploadArea} htmlFor="producto-upload">
                <input 
                  type="file" 
                  id="producto-upload" 
                  hidden
                  accept="image/*"
                  onChange={(e) => handleImageChange(e, 'logo')} 
                />
                {(previews.logo || formData.imagen) ? (
                  <img 
                    src={previews.logo || formData.imagen} 
                    alt="Producto" 
                    className={styles.imagePreview} 
                    style={{ width: '100%', height: '100%', objectFit: 'cover', borderRadius: 'inherit' }}
                  />
                ) : (
                  <div style={{display:'flex', alignItems:'center', justifyContent:'center', width:'100%', height:'100%'}}>
                    <svg width="40" height="40" viewBox="0 0 24 24" fill="#9da0a4">
                      <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                    </svg>
                  </div>
                )}
              </label>
              <p className={styles.imageLabel}>Imagen del producto</p>
            </div>

            <form className={styles.form} onSubmit={handleSubmit}>
              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Nombre del producto</label>
                <textarea
                  name="nombre"
                  className={styles.formTextarea}
                  value={formData.nombre}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  rows={2}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Descripción</label>
                <textarea
                  name="descripcion"
                  className={styles.formTextarea}
                  value={formData.descripcion}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  rows={5}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Precio</label>
                <input
                  type="number"
                  name="precio"
                  className={styles.formInput}
                  value={formData.precio}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Categoría</label>
                <select
                  name="categoria"
                  className={styles.formSelect}
                  value={formData.categoria}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                >
                  <option value=""></option>
                  <option value="Comida">Comida</option>
                  <option value="Bebida">Bebida</option>
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Subcategoría</label>
                <select
                  name="subcategoria"
                  className={styles.formSelect}
                  value={formData.subcategoria}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                >
                  <option value=""></option>
                  {subcategoriasList.map((sub) => (
                    <option key={sub} value={sub}>
                      {sub}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Disponible</label>
                <select
                  name="disponible"
                  className={styles.formSelect}
                  value={formData.disponible}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                >
                  <option value=""></option>
                  <option value="Disponible">Disponible</option>
                  <option value="No disponible">No disponible</option>
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.formLabel}>Observaciones</label>
                <textarea
                  name="observaciones"
                  className={styles.formTextarea}
                  value={formData.observaciones}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  rows={3}
                />
              </div>

              {isEditing && (
                <button type="submit" className={styles.submitBtn}>
                  Guardar
                </button>
              )}
            </form>
          </div>
        </div>
      </main>

      {/* FOOTER */}
      <footer className={styles.footer}>
        <div className={`${styles.container} ${styles.footerInner}`}>
          <p className={styles.footerText}>PediloYa © 2026. Todos los derechos reservados.</p>
        </div>
      </footer>

      {/* FILTERS MODAL */}
      {showFilters && (
        <div className={styles.modalOverlay} onClick={() => setShowFilters(false)}>
          <div className={styles.filtersModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.filtersHeader}>
              <h2 className={styles.filtersTitle}>Filtros ({filterCount})</h2>
              <button className={styles.closeBtn} onClick={() => setShowFilters(false)}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="#374151">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
                </svg>
              </button>
            </div>

            <div className={styles.filtersBody}>
              {/* Categorías */}
              <div className={styles.filterSection}>
                <div className={styles.filterSectionHeader}>
                  <span className={styles.filterSectionTitle}>Categorías</span>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                    <path d="M19 13H5v-2h14v2z" />
                  </svg>
                </div>
                <div className={styles.filterOptions}>
                  <label className={styles.filterOption}>
                    <input
                      type="checkbox"
                      checked={filters.categorias.includes("Comida")}
                      onChange={() => handleFilterChange("categorias", "Comida")}
                    />
                    <span>Comida</span>
                  </label>
                  <label className={styles.filterOption}>
                    <input
                      type="checkbox"
                      checked={filters.categorias.includes("Bebida")}
                      onChange={() => handleFilterChange("categorias", "Bebida")}
                    />
                    <span>Bebida</span>
                  </label>
                </div>
              </div>

              {/* Subcategorías */}
              <div className={styles.filterSection}>
                <div className={styles.filterSectionHeader}>
                  <span className={styles.filterSectionTitle}>Subcategorías</span>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                    <path d="M19 13H5v-2h14v2z" />
                  </svg>
                </div>
                <div className={styles.filterOptionsGrid}>
                  {subcategoriasList.map((sub) => (
                    <label key={sub} className={styles.filterOption}>
                      <input
                        type="checkbox"
                        checked={filters.subcategorias.includes(sub)}
                        onChange={() => handleFilterChange("subcategorias", sub)}
                      />
                      <span>{sub}</span>
                    </label>
                  ))}
                </div>
              </div>

              {/* Disponibilidad */}
              <div className={styles.filterSection}>
                <div className={styles.filterSectionHeader}>
                  <span className={styles.filterSectionTitle}>Disponibilidad</span>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                    <path d="M19 13H5v-2h14v2z" />
                  </svg>
                </div>
                <div className={styles.filterOptions}>
                  <label className={styles.filterOption}>
                    <input
                      type="checkbox"
                      checked={filters.disponibilidad.includes("Disponible")}
                      onChange={() => handleFilterChange("disponibilidad", "Disponible")}
                    />
                    <span>Disponible</span>
                  </label>
                  <label className={styles.filterOption}>
                    <input
                      type="checkbox"
                      checked={filters.disponibilidad.includes("No disponible")}
                      onChange={() => handleFilterChange("disponibilidad", "No disponible")}
                    />
                    <span>No disponible</span>
                  </label>
                </div>
              </div>
            </div>

            <div className={styles.filtersFooter}>
              <button className={styles.applyBtn} onClick={() => setShowFilters(false)}>
                Aplicar
              </button>
              <button className={styles.clearBtn} onClick={clearFilters}>
                Limpiar filtros
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
