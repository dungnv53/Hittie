2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  期間: 0.149 ミリ秒
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  期間: 0.187 ミリ秒
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:13:41 ICT netreal-netreal_test LOG:  期間: 0.192 ミリ秒
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  期間: 0.202 ミリ秒
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:13:58 ICT netreal-netreal_test LOG:  期間: 0.193 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.192 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT 
	                                login_id ,     
	                                login_pass    ,
	                                partner_tickets.affiliater_id 
	                            FROM 
	                                partner_ticket_members 
	                                    LEFT JOIN 
	                                        partner_tickets ON 
	                                            partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                            WHERE 
	                                login_id    = 'NF105001'   AND 
	                                login_pass  = 'FdmdrjZL' AND 
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-03 14:14:03')
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.267 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.117 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.163 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.136 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389337948', "user_data" = '' WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.534 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389337948', "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.427 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389337948', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.378 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.084 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389337948', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.648 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.196 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.192 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'faf980c4a00edb37f82da14b633cdb75' 
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.105 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.187 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.168 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.151 ミリ秒
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 14:14:03 ICT netreal-netreal_test LOG:  期間: 0.224 ミリ秒
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'faf980c4a00edb37f82da14b633cdb75'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  期間: 0.184 ミリ秒
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  期間: 0.146 ミリ秒
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  期間: 0.134 ミリ秒
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  文: SELECT "tel_services"."send_date_start" as send_date_start, "services"."title" as title, "tel_services"."call_ok" as call_ok, "tel_services"."call_ng" as call_ng, "lists"."rows" as rows, "services"."status" as services_status, "tel_services"."status" as tel_services_status
	FROM "tel_services", "services", "lists"
	WHERE "services"."service_type" = 'TEL'
	AND "services"."member_id" = '7'
	AND "services"."id" = tel_services.service_id
	AND "services"."status" != 8
	ORDER BY "tel_services"."send_date_start" desc
	LIMIT 20
2014-01-10 14:14:05 ICT netreal-netreal_test LOG:  期間: 0.405 ミリ秒