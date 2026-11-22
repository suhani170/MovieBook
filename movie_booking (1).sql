-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 22, 2026 at 10:05 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `movie_booking`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`admin_id`, `username`, `password`) VALUES
(1, 'admin', 'PBKDF2$210000$ZdAUipKPav89/o7mtuI/kg==$skoQU68ioeucIlZc2l4I1ypbph0x/C9X8dENAVnwd9I=');

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `show_id` int(11) DEFAULT NULL,
  `seat_number` varchar(10) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `booking_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`booking_id`, `user_id`, `show_id`, `seat_number`, `total_amount`, `booking_date`) VALUES
(26, 7, 9, 'A1', 300.00, '2026-11-18 10:17:15'),
(27, 7, 9, 'A2', 300.00, '2026-11-18 10:17:15'),
(28, 7, 10, 'F2', 250.00, '2026-11-19 05:51:46'),
(29, 7, 10, 'F3', 250.00, '2026-11-19 05:51:46'),
(30, 7, 11, 'A1', 300.00, '2026-11-19 12:07:44'),
(31, 7, 11, 'C3', 300.00, '2026-11-19 12:07:44'),
(32, 7, 11, 'A2', 300.00, '2026-11-19 12:07:44'),
(33, 7, 11, 'A3', 300.00, '2026-11-19 12:07:44'),
(34, 7, 13, 'E6', 250.00, '2026-11-20 05:56:53'),
(35, 7, 13, 'E7', 250.00, '2026-11-20 05:56:53'),
(36, 7, 13, 'A1', 250.00, '2026-11-20 15:05:25'),
(37, 7, 13, 'A2', 250.00, '2026-11-20 15:05:25');

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `movie_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `genre` varchar(500) DEFAULT NULL,
  `language` varchar(50) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `poster_url` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`movie_id`, `title`, `genre`, `language`, `duration`, `description`, `poster_url`) VALUES
(6, 'Avatar', 'Sci-Fi', 'English', 162, 'A science fiction adventure movie.', 'https://image.tmdb.org/t/p/w500/kyeqWdyUXW608qlYkRqosgbbJyK.jpg'),
(13, 'Interstellar', 'Sci-Fi', 'EN', 60, 'The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.', 'https://image.tmdb.org/t/p/w500/yQvGrMoipbRoddT0ZR8tPoR7NfX.jpg'),
(16, 'Avatar: Fire and Ash', 'Science Fiction, Adventure, Fantasy', 'EN', 198, 'In the wake of the devastating war against the RDA and the loss of their eldest son, Jake Sully and Neytiri face a new threat on Pandora: the Ash People, a violent and power-hungry Na\'vi tribe led by the ruthless Varang. Jake\'s family must fight for their survival and the future of Pandora in a conflict that pushes them to their emotional and physical limits.', 'https://image.tmdb.org/t/p/w500/bRBeSHfGHwkEpImlhxPmOcUsaeg.jpg'),
(17, 'Alpha', 'Action, Thriller', 'HI', 141, 'When Sita, a highly trained assassin raised in isolation as a super-soldier by a rogue commander, discovers the dark truth about her family and her stolen childhood, she teams up with her long lost sister to take down her creator and his illicit military program.', 'https://image.tmdb.org/t/p/w500/bPtRt3ajQ0EkyeQ1O6iJwAIi9Py.jpg'),
(18, 'Avatar: The Way of Water', 'Action, Adventure, Science Fiction', 'EN', 192, 'Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.', 'https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg'),
(20, 'Ne Zha 2', 'Animation, Action, Fantasy, Adventure', 'ZH', 144, 'After a catastrophic event leaves their bodies destroyed, Ne Zha and Ao Bing are granted a fragile second chance at life. As tensions rise between the dragon clans and celestial forces, the two must undergo a series of perilous trials that will test their bond, challenge their identities, and decide the fate of both mortals and immortals.', 'https://image.tmdb.org/t/p/w500/fcs5AdCKliQaQgXsPS9kDejamIJ.jpg'),
(21, 'Ne Zha', 'Animation, Fantasy, Adventure', 'ZH', 110, 'A young boy is born as the reincarnation of a demonic power, into a society that hates and fears him. Destined by prophecy to bring destruction to the world, Nezha must choose between good and evil to see if he can change his fate.', 'https://image.tmdb.org/t/p/w500/phM9bb6s9c60LA8qwsdk7U1N2cS.jpg'),
(22, 'Hoppers', 'Animation, Adventure, Comedy, Family, Science Fiction', 'EN', 104, 'Scientists have discovered how to \'hop\' human consciousness into lifelike robotic animals, allowing people to communicate with animals as animals. Animal lover Mabel seizes an opportunity to use the technology, uncovering mysteries within the animal world beyond anything she could have imagined.', 'https://image.tmdb.org/t/p/w500/xjtWQ2CL1mpmMNwuU5HeS4Iuwuu.jpg'),
(23, 'Stand by Me Doraemon', 'Animation, Adventure, Science Fiction, Drama, Family, Fantasy', 'JA', 94, 'Sewashi and Doraemon find themselves way back in time and meet Nobita. It is up to Doraemon to take care of Nobita or else he will not return to the present.', 'https://image.tmdb.org/t/p/w500/wc7XQbfx6EIQqCuvmBMt3aisb2Y.jpg'),
(25, 'The Hobbit: An Unexpected Journey', 'Adventure, Fantasy, Action', 'EN', 169, 'Bilbo Baggins, a hobbit enjoying his quiet life, is swept into an epic quest by Gandalf the Grey and thirteen dwarves who seek to reclaim their mountain home from Smaug, the dragon.', 'https://image.tmdb.org/t/p/w500/yHA9Fc37VmpUA5UncTxxo3rTGVA.jpg'),
(26, 'Spider-Man: Brand New Day', 'Science Fiction, Action, Adventure', 'EN', 145, 'Fighting crime full-time as Spider-Man in a world that doesn\'t remember him—and the pressure of seeing his old friends move on without him—sparks a change in Peter Parker he may not have the power to control. But that transformation might also be the only thing that can stop a shocking new threat to the city and those he loves - a powerful villain no one can even see.', 'https://image.tmdb.org/t/p/w500/bjiS5ipwxb9JFy3XRRN4OAilSeX.jpg'),
(27, 'Spider-Man 2', 'Action, Adventure, Science Fiction', 'EN', 127, 'Peter Parker is going through a major identity crisis. Burned out from being Spider-Man, he decides to shelve his superhero alter ego, which leaves the city suffering in the wake of carnage left by the evil Doc Ock. In the meantime, Parker still can\'t act on his feelings for Mary Jane Watson, a girl he\'s loved since childhood. A certain anger begins to brew in his best friend Harry Osborn as well...', 'https://image.tmdb.org/t/p/w500/aGuvNAaaZuWXYQQ6N2v7DeuP6mB.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `shows`
--

CREATE TABLE `shows` (
  `show_id` int(11) NOT NULL,
  `movie_id` int(11) DEFAULT NULL,
  `theatre_id` int(11) DEFAULT NULL,
  `show_date` date DEFAULT NULL,
  `show_time` time DEFAULT NULL,
  `ticket_price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shows`
--

INSERT INTO `shows` (`show_id`, `movie_id`, `theatre_id`, `show_date`, `show_time`, `ticket_price`) VALUES
(8, 6, 4, '2026-11-18', '00:00:00', 300.00),
(9, 13, 4, '2026-11-18', '19:00:00', 300.00),
(10, 17, 4, '2026-11-19', '12:00:00', 250.00),
(11, 20, 5, '2026-11-25', '11:00:00', 300.00),
(12, 25, 5, '2026-11-25', '23:00:00', 200.00),
(13, 23, 4, '2026-11-23', '11:30:00', 250.00);

-- --------------------------------------------------------

--
-- Table structure for table `theatres`
--

CREATE TABLE `theatres` (
  `theatre_id` int(11) NOT NULL,
  `theatre_name` varchar(100) NOT NULL,
  `location` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `theatres`
--

INSERT INTO `theatres` (`theatre_id`, `theatre_name`, `location`) VALUES
(4, 'PVR Cinemas', 'Ahmedabad'),
(5, 'inox', 'rajkot');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `email`, `password`) VALUES
(6, 'suhani', 'suhani.makwana121091@marwadiuniversity.ac.in', '12345'),
(7, 'suhani', 'makwanasuhani19@gmail.com', 'PBKDF2$210000$7oTIxzbZJ1qEMMdzgsR5AQ==$3/oDHmLUy8jVA1bp6rN76lHp3BQ8SVgqCIJ6hvNhPvc='),
(9, 'suhani makwana', 'makwanasuhani03@gmail.com', 'PBKDF2$210000$6DFbc1pihy0gy/0gdROyZw==$9V1vShjIn8419WKX2QcF3c+dP8PylcOiYtmRPB7u3nc='),
(10, 'suhani makwana', 'suhanimakwana03@gmail.com', 'PBKDF2$210000$9PneX9ZGd9usD1dDCPGvkg==$qsDqG+HrDnQYXAJ+rz2qodvDojk4a2TSKLyomf76scc=');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`),
  ADD UNIQUE KEY `uq_show_seat` (`show_id`,`seat_number`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`movie_id`);

--
-- Indexes for table `shows`
--
ALTER TABLE `shows`
  ADD PRIMARY KEY (`show_id`),
  ADD KEY `movie_id` (`movie_id`),
  ADD KEY `theatre_id` (`theatre_id`);

--
-- Indexes for table `theatres`
--
ALTER TABLE `theatres`
  ADD PRIMARY KEY (`theatre_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `movie_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `shows`
--
ALTER TABLE `shows`
  MODIFY `show_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `theatres`
--
ALTER TABLE `theatres`
  MODIFY `theatre_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`show_id`) REFERENCES `shows` (`show_id`);

--
-- Constraints for table `shows`
--
ALTER TABLE `shows`
  ADD CONSTRAINT `shows_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`movie_id`),
  ADD CONSTRAINT `shows_ibfk_2` FOREIGN KEY (`theatre_id`) REFERENCES `theatres` (`theatre_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
