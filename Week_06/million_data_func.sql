delimiter $$
CREATE PROCEDURE multiInsertM (IN args INT)
BEGIN
	DECLARE
		i INT DEFAULT 100001 ; START TRANSACTION ;
	WHILE i <= args DO
		INSERT INTO tb_order (
			order_id,
			order_sn,
			user_id,
			business_id,
			product_snapshot,
			pay_amount,
			order_status,
			note,
			delete_status,
			payment_time,
			modify_time,
			create_time
		)
	VALUES
		(
			i,
			concat("geekbang-order-", i),
			i,
			i,
			concat(
				"resource\\geekbang-snapshot-\\",
				i
			),
			130.00,
			1,
			concat("u.geekbang-", i),
			0,
			now(),
			now(),
			now()
		) ;
	SET i = i + 1 ;
	END
	WHILE ; COMMIT ;
	END$$

delimiter ;