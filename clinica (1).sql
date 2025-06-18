-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-06-2025 a las 06:08:32
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `clinica`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
  `id_cita` int(11) NOT NULL,
  `id_paciente` int(11) DEFAULT NULL,
  `id_doctor` int(11) DEFAULT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `estado` int(11) NOT NULL,
  `motivo_consulta` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cita`
--

INSERT INTO `cita` (`id_cita`, `id_paciente`, `id_doctor`, `fecha`, `hora`, `estado`, `motivo_consulta`) VALUES
(1, 1, 5, '2025-06-19', '17:30:00', 1, 'mensaje de prueba'),
(2, 1, 7, '2025-06-21', '11:00:00', 1, 'mensaje de prueba'),
(3, 1, 2, '2025-06-19', '15:30:00', 1, 'mensaje de prueba 1'),
(4, 1, 13, '2025-06-23', '10:30:00', 1, 'dolor intenso en el pecho');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `doctor`
--

CREATE TABLE `doctor` (
  `id_doctor` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `ap_paterno` varchar(255) DEFAULT NULL,
  `ap_materno` varchar(255) DEFAULT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `id_especialidad` int(11) DEFAULT NULL,
  `estado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `doctor`
--

INSERT INTO `doctor` (`id_doctor`, `nombre`, `ap_paterno`, `ap_materno`, `dni`, `telefono`, `id_especialidad`, `estado`) VALUES
(1, 'Luis', 'Ramírez', 'Gonzales', '71456321', '987654321', 1, 1),
(2, 'Ana', 'Torres', 'Salazar', '45879632', '912345678', 1, 1),
(3, 'María', 'Fernández', 'Castillo', '46781259', '924563218', 1, 1),
(4, 'Carmen', 'Quispe', 'Ramos', '50123698', '911222333', 1, 1),
(5, 'José', 'Mendoza', 'Lozano', '42369874', '988877766', 2, 1),
(6, 'Carlos', 'Reyes', 'Córdova', '74852136', '913334455', 2, 1),
(7, 'Andrea', 'Paredes', 'Mamani', '67891245', '976543210', 2, 1),
(8, 'Raúl', 'Huamán', 'Vargas', '45673829', '901112233', 3, 1),
(9, 'Lucía', 'Peralta', 'Campos', '42984571', '916789432', 3, 1),
(10, 'Juan', 'Escobar', 'Alarcón', '73365419', '955667788', 4, 1),
(11, 'Diego', 'López', 'Arce', '45128736', '965432198', 4, 1),
(12, 'Pamela', 'Rojas', 'Chávez', '42753689', '983332211', 5, 1),
(13, 'Fernando', 'Galván', 'Morales', '49978563', '990011223', 5, 1),
(14, 'Rocío', 'Aguilar', 'Poma', '45219387', '998877665', 5, 1),
(15, 'Luis', 'Sánchez', 'Rosales', '42568931', '987654987', 5, 1),
(16, 'Gabriela', 'Silva', 'Reátegui', '41125638', '973214569', 1, 1),
(17, 'Oscar', 'Delgado', 'Paredes', '49875412', '956789012', 6, 1),
(18, 'Milagros', 'Espinoza', 'Nieto', '44579823', '911223344', 6, 1),
(19, 'Elena', 'Navarro', 'Huerta', '41975326', '922334455', 6, 1),
(20, 'Jorge', 'Vega', 'Ticona', '43218795', '933445566', 6, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `especialidad`
--

CREATE TABLE `especialidad` (
  `id_especialidad` int(11) NOT NULL,
  `nombre_especialidad` varchar(255) DEFAULT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `especialidad`
--

INSERT INTO `especialidad` (`id_especialidad`, `nombre_especialidad`, `descripcion`) VALUES
(1, 'Medicina General', 'Atención médica básica, diagnóstico inicial y derivación a especialistas.'),
(2, 'Pediatría', 'Atención médica para bebés, niños y adolescentes.'),
(3, 'Ginecología', 'Salud del sistema reproductor femenino y control prenatal.'),
(4, 'Obstetricia', 'Atención al embarazo, parto y postparto.'),
(5, 'Cardiología', 'Diagnóstico y tratamiento de enfermedades del corazón y sistema circulatorio.'),
(6, 'Dermatología', 'Tratamiento de enfermedades de la piel, cabello y uñas.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_cita`
--

CREATE TABLE `estado_cita` (
  `id` int(11) NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estado_cita`
--

INSERT INTO `estado_cita` (`id`, `descripcion`) VALUES
(1, 'Pendiente'),
(2, 'Confirmada'),
(3, 'Cancelada'),
(4, 'Atendida');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horario_doctor`
--

CREATE TABLE `horario_doctor` (
  `id_horario` int(11) NOT NULL,
  `id_doctor` int(11) DEFAULT NULL,
  `dia_semana` varchar(100) DEFAULT NULL,
  `hora_inicio` time DEFAULT NULL,
  `hora_fin` time DEFAULT NULL,
  `estado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `horario_doctor`
--

INSERT INTO `horario_doctor` (`id_horario`, `id_doctor`, `dia_semana`, `hora_inicio`, `hora_fin`, `estado`) VALUES
(1, 1, 'Lunes', '08:00:00', '12:00:00', 1),
(2, 1, 'Martes', '08:00:00', '12:00:00', 1),
(3, 1, 'Miércoles', '08:00:00', '12:00:00', 1),
(4, 1, 'Jueves', '08:00:00', '12:00:00', 1),
(5, 1, 'Viernes', '08:00:00', '12:00:00', 1),
(6, 2, 'Martes', '14:00:00', '18:00:00', 1),
(7, 2, 'Jueves', '14:00:00', '18:00:00', 1),
(8, 3, 'Lunes', '09:00:00', '13:00:00', 1),
(9, 3, 'Miércoles', '09:00:00', '13:00:00', 1),
(10, 3, 'Viernes', '09:00:00', '13:00:00', 1),
(11, 4, 'Martes', '08:00:00', '12:00:00', 1),
(12, 4, 'Sábado', '08:00:00', '12:00:00', 1),
(13, 5, 'Lunes', '15:00:00', '19:00:00', 1),
(14, 5, 'Martes', '15:00:00', '19:00:00', 1),
(15, 5, 'Miércoles', '15:00:00', '19:00:00', 1),
(16, 5, 'Jueves', '15:00:00', '19:00:00', 1),
(17, 6, 'Miércoles', '08:00:00', '17:00:00', 1),
(18, 7, 'Viernes', '14:00:00', '18:00:00', 1),
(19, 7, 'Sábado', '08:00:00', '12:00:00', 1),
(20, 8, 'Lunes', '08:00:00', '12:00:00', 1),
(21, 8, 'Martes', '08:00:00', '12:00:00', 1),
(22, 8, 'Miércoles', '08:00:00', '12:00:00', 1),
(23, 8, 'Jueves', '08:00:00', '12:00:00', 1),
(24, 8, 'Viernes', '08:00:00', '12:00:00', 1),
(25, 9, 'Lunes', '14:00:00', '18:00:00', 1),
(26, 9, 'Miércoles', '14:00:00', '18:00:00', 1),
(27, 9, 'Viernes', '14:00:00', '18:00:00', 1),
(28, 10, 'Sábado', '08:00:00', '17:00:00', 1),
(29, 1, 'Lunes', '08:00:00', '12:00:00', 1),
(30, 1, 'Martes', '08:00:00', '12:00:00', 1),
(31, 1, 'Miércoles', '08:00:00', '12:00:00', 1),
(32, 1, 'Jueves', '08:00:00', '12:00:00', 1),
(33, 1, 'Viernes', '08:00:00', '12:00:00', 1),
(34, 2, 'Martes', '14:00:00', '18:00:00', 1),
(35, 2, 'Jueves', '14:00:00', '18:00:00', 1),
(36, 3, 'Lunes', '09:00:00', '13:00:00', 1),
(37, 3, 'Miércoles', '09:00:00', '13:00:00', 1),
(38, 3, 'Viernes', '09:00:00', '13:00:00', 1),
(39, 4, 'Martes', '08:00:00', '12:00:00', 1),
(40, 4, 'Sábado', '08:00:00', '12:00:00', 1),
(41, 5, 'Lunes', '15:00:00', '19:00:00', 1),
(42, 5, 'Martes', '15:00:00', '19:00:00', 1),
(43, 5, 'Miércoles', '15:00:00', '19:00:00', 1),
(44, 5, 'Jueves', '15:00:00', '19:00:00', 1),
(45, 6, 'Miércoles', '08:00:00', '17:00:00', 1),
(46, 7, 'Viernes', '14:00:00', '18:00:00', 1),
(47, 7, 'Sábado', '08:00:00', '12:00:00', 1),
(48, 8, 'Lunes', '08:00:00', '12:00:00', 1),
(49, 8, 'Martes', '08:00:00', '12:00:00', 1),
(50, 8, 'Miércoles', '08:00:00', '12:00:00', 1),
(51, 8, 'Jueves', '08:00:00', '12:00:00', 1),
(52, 8, 'Viernes', '08:00:00', '12:00:00', 1),
(53, 9, 'Lunes', '14:00:00', '18:00:00', 1),
(54, 9, 'Miércoles', '14:00:00', '18:00:00', 1),
(55, 9, 'Viernes', '14:00:00', '18:00:00', 1),
(56, 10, 'Sábado', '08:00:00', '17:00:00', 1),
(57, 11, 'Lunes', '07:00:00', '11:00:00', 1),
(58, 11, 'Martes', '07:00:00', '11:00:00', 1),
(59, 11, 'Miércoles', '07:00:00', '11:00:00', 1),
(60, 11, 'Jueves', '07:00:00', '11:00:00', 1),
(61, 11, 'Viernes', '07:00:00', '11:00:00', 1),
(62, 12, 'Martes', '14:30:00', '18:30:00', 1),
(63, 12, 'Miércoles', '14:30:00', '18:30:00', 1),
(64, 12, 'Jueves', '14:30:00', '18:30:00', 1),
(65, 13, 'Lunes', '08:30:00', '12:30:00', 1),
(66, 13, 'Viernes', '08:30:00', '12:30:00', 1),
(67, 14, 'Sábado', '08:00:00', '16:00:00', 1),
(68, 15, 'Miércoles', '08:00:00', '12:00:00', 1),
(69, 15, 'Miércoles', '14:00:00', '18:00:00', 1),
(70, 15, 'Viernes', '08:00:00', '12:00:00', 1),
(71, 15, 'Viernes', '14:00:00', '18:00:00', 1),
(72, 16, 'Lunes', '13:00:00', '17:00:00', 1),
(73, 16, 'Martes', '13:00:00', '17:00:00', 1),
(74, 16, 'Miércoles', '13:00:00', '17:00:00', 1),
(75, 16, 'Jueves', '13:00:00', '17:00:00', 1),
(76, 17, 'Martes', '08:00:00', '12:00:00', 1),
(77, 17, 'Viernes', '08:00:00', '12:00:00', 1),
(78, 18, 'Lunes', '08:00:00', '11:00:00', 1),
(79, 18, 'Martes', '08:00:00', '11:00:00', 1),
(80, 18, 'Miércoles', '08:00:00', '11:00:00', 1),
(81, 18, 'Jueves', '08:00:00', '11:00:00', 1),
(82, 18, 'Viernes', '08:00:00', '11:00:00', 1),
(83, 18, 'Sábado', '08:00:00', '11:00:00', 1),
(84, 19, 'Martes', '15:00:00', '19:00:00', 1),
(85, 19, 'Jueves', '15:00:00', '19:00:00', 1),
(86, 20, 'Sábado', '09:00:00', '13:00:00', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

CREATE TABLE `paciente` (
  `id_paciente` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `dni` varchar(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`id_paciente`, `nombre`, `apellido`, `dni`, `telefono`, `email`, `password`) VALUES
(1, 'Matias', 'Gonzales Fong', '71839953', '926381223', 'matias@gmail.com', '$2y$10$2.tJZjx5TKMQO.6Vi2TxtuUq1UArmZZGaD9F7GiYitRXnwLn77nHu');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipocita`
--

CREATE TABLE `tipocita` (
  `id_tipocita` int(11) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cita`
--
ALTER TABLE `cita`
  ADD PRIMARY KEY (`id_cita`),
  ADD KEY `fk_cita_paciente` (`id_paciente`),
  ADD KEY `fk_cita_doctor` (`id_doctor`) USING BTREE;

--
-- Indices de la tabla `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`id_doctor`),
  ADD KEY `fk_doctor_especialidad` (`id_especialidad`);

--
-- Indices de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  ADD PRIMARY KEY (`id_especialidad`);

--
-- Indices de la tabla `estado_cita`
--
ALTER TABLE `estado_cita`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `horario_doctor`
--
ALTER TABLE `horario_doctor`
  ADD PRIMARY KEY (`id_horario`),
  ADD KEY `fk_horario_doctor` (`id_doctor`);

--
-- Indices de la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD PRIMARY KEY (`id_paciente`);

--
-- Indices de la tabla `tipocita`
--
ALTER TABLE `tipocita`
  ADD PRIMARY KEY (`id_tipocita`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
  MODIFY `id_cita` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `doctor`
--
ALTER TABLE `doctor`
  MODIFY `id_doctor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `especialidad`
--
ALTER TABLE `especialidad`
  MODIFY `id_especialidad` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `estado_cita`
--
ALTER TABLE `estado_cita`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `horario_doctor`
--
ALTER TABLE `horario_doctor`
  MODIFY `id_horario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT de la tabla `paciente`
--
ALTER TABLE `paciente`
  MODIFY `id_paciente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `tipocita`
--
ALTER TABLE `tipocita`
  MODIFY `id_tipocita` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `fk_cita_doctor` FOREIGN KEY (`id_doctor`) REFERENCES `doctor` (`id_doctor`),
  ADD CONSTRAINT `fk_cita_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`);

--
-- Filtros para la tabla `doctor`
--
ALTER TABLE `doctor`
  ADD CONSTRAINT `fk_doctor_especialidad` FOREIGN KEY (`id_especialidad`) REFERENCES `especialidad` (`id_especialidad`);

--
-- Filtros para la tabla `horario_doctor`
--
ALTER TABLE `horario_doctor`
  ADD CONSTRAINT `fk_horario_doctor` FOREIGN KEY (`id_doctor`) REFERENCES `doctor` (`id_doctor`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
