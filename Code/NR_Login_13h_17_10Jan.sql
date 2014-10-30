2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.164 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: INSERT INTO "ci_sessions" ("session_id", "ip_address", "user_agent", "last_activity") VALUES ('37e0db1ea635917cdb97596cc8d68e96', '118.70.171.28', 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;', 1389334650)
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.892 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT 
	                                login_id ,     
	                                login_pass    ,
	                                partner_tickets.affiliater_id 
	                            FROM 
	                                partner_ticket_members 
	                                    LEFT JOIN 
	                                        partner_tickets ON 
	                                            partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                            WHERE 
	                                login_id    = 'NF109001'   AND 
	                                login_pass  = 'nHWhs3jR' AND 
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-30 13:17:30')
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.265 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF109001' AND login_pass = 'nHWhs3jR'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.117 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF109001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.155 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF109001'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.252 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389334650, "user_data" = '' WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 2.072 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389334650, "user_data" = 'a:1:{s:9:"member_id";s:1:"8";}' WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.297 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389334650, "user_data" = 'a:2:{s:9:"member_id";s:1:"8";s:8:"login_id";s:8:"NF109001";}' WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.358 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 8
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.177 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389334650, "user_data" = 'a:3:{s:9:"member_id";s:1:"8";s:8:"login_id";s:8:"NF109001";s:34:"fax_price_master_dear_member_price";s:2:"54";}' WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.350 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.186 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
	AND "user_agent" = 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.494 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = '37e0db1ea635917cdb97596cc8d68e96' 
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.102 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.189 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
	AND "user_agent" = 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.248 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT
	                                    *
	                                FROM
	                                    ci_sessions
	                                WHERE
	                                    ci_sessions.session_id = '21cf6d21a75e3d39c23d2928655200ef'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.113 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: UPDATE
	                                    ci_sessions
	                                        SET
	                                            ip_address    = '118.70.171.28' ,
	                                            user_agent    = 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;' ,
	                                            last_activity = '1389325401' ,
	                                            user_data     = ''
	                                        WHERE
	                                            session_id  = '37e0db1ea635917cdb97596cc8d68e96'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.919 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '8'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.392 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.258 ミリ秒
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '8'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 13:17:30 ICT netreal-netreal_test LOG:  期間: 0.170 ミリ秒











2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  期間: 0.183 ミリ秒
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
	AND "user_agent" = 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;'
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  期間: 0.300 ミリ秒
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  期間: 0.173 ミリ秒
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '37e0db1ea635917cdb97596cc8d68e96'
	AND "user_agent" = 'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1;'
2014-01-10 13:18:33 ICT netreal-netreal_test LOG:  期間: 0.298 ミリ秒