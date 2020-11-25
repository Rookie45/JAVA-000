CREATE  PROCEDURE multiInsertM(INOUT x int)
LANGUAGE plpgsql
AS $$
DECLARE i int :=1;
	BEGIN	
	FOR i in 1..x LOOP
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
			'geekbang-order-'|| i,
			i,
			i,
			'resource\\geekbang-snapshot-\\'||i,
			130.00,
			1,
			'u.geekbang-'|| i,
			0,
			now(),
			now(),
			now()
		) ;
	END LOOP;
	END;
$$;
CALL multiInsertM(1000000);
