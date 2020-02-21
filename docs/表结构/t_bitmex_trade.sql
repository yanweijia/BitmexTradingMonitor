CREATE TABLE `t_bitmex_trade` (
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `symbol` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `side` varchar(11) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `size` int(11) NOT NULL,
  `price` decimal(11,0) NOT NULL,
  `tick_direction` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `trd_match_id` varchar(70) COLLATE utf8mb4_bin DEFAULT NULL,
  `gross_value` bigint(11) DEFAULT NULL,
  `home_notional` decimal(10,0) DEFAULT NULL,
  `foreign_notional` int(11) DEFAULT NULL,
  UNIQUE KEY `trd_match_id` (`trd_match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;