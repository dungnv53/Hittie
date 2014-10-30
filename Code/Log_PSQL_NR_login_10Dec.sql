2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.350 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.116 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.237 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.151 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325480', "user_data" = '' WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.826 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325480', "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.534 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325480', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.606 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325480', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.561 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.194 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.192 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'bddde457581b91a9f2f147475a3dd2fc' 
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.697 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.438 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT
	                                    *
	                                FROM
	                                    ci_sessions
	                                WHERE
	                                    ci_sessions.session_id = 'b061ce1b77e4b16c4612a9ebf14c3ecd'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.103 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: UPDATE
	                                    ci_sessions
	                                        SET
	                                            ip_address    = '118.70.171.28' ,
	                                            user_agent    = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K' ,
	                                            last_activity = '1389325356' ,
	                                            user_data     = ''
	                                        WHERE
	                                            session_id  = 'bddde457581b91a9f2f147475a3dd2fc'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.832 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.142 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.123 ミリ秒
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:44:51 ICT netreal-netreal_test LOG:  期間: 0.113 ミリ秒
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  期間: 0.778 ミリ秒
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  期間: 0.201 ミリ秒
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'bddde457581b91a9f2f147475a3dd2fc'
	AND "user_agent" = 'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (K'
2014-01-10 10:44:53 ICT netreal-netreal_test LOG:  期間: 0.193 ミリ秒
2014-01-10 10:45:51 ICT postgres-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-51 10:44:51');
2014-01-10 10:45:51 ICT postgres-netreal_test LOG:  期間: 35.464 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.179 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '95db8cfb5c12da6f035fa5366f3e9663'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 1.387 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389325727, "session_id" = 'a51e1876b11338c4f21350899b019626' WHERE session_id = '95db8cfb5c12da6f035fa5366f3e9663'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 2.097 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-47 10:48:47')
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.520 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.119 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.177 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.137 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389325727, "user_data" = '' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.705 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389325727, "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.523 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389325727, "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.585 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.077 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = 1389325727, "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.557 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.194 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.180 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.299 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'a51e1876b11338c4f21350899b019626' 
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.251 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.177 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.349 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT
	                                    *
	                                FROM
	                                    ci_sessions
	                                WHERE
	                                    ci_sessions.session_id = '0674b2787335f8d2a1d0b141040ca196'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.109 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: UPDATE
	                                    ci_sessions
	                                        SET
	                                            ip_address    = '118.70.171.28' ,
	                                            user_agent    = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26' ,
	                                            last_activity = '1389322018' ,
	                                            user_data     = 'a:1:{s:34:"fax_price_master_dear_member_price";s:2:"54";}'
	                                        WHERE
	                                            session_id  = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.941 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.301 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.226 ミリ秒
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:48:47 ICT netreal-netreal_test LOG:  期間: 0.158 ミリ秒
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  期間: 0.160 ミリ秒
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  期間: 0.284 ミリ秒
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  期間: 0.172 ミリ秒
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:50 ICT netreal-netreal_test LOG:  期間: 0.291 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.202 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 1.002 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-53 10:48:53')
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.268 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.114 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.153 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.137 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = 'a:1:{s:34:"fax_price_master_dear_member_price";s:2:"54";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.604 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = 'a:2:{s:34:"fax_price_master_dear_member_price";s:2:"54";s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.314 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = 'a:3:{s:34:"fax_price_master_dear_member_price";s:2:"54";s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.210 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.077 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.551 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.191 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.170 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.425 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'a51e1876b11338c4f21350899b019626' 
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.145 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.080 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.188 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.159 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.128 ミリ秒
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:48:53 ICT netreal-netreal_test LOG:  期間: 0.115 ミリ秒
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  期間: 0.185 ミリ秒
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  期間: 0.291 ミリ秒
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  期間: 0.141 ミリ秒
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  期間: 0.120 ミリ秒
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  文: SELECT "fax_services"."demand_date" as demand_date, "services"."title" as title, "fax_services"."send_ok" as send_ok, "fax_services"."send_ng" as send_ng, "lists"."rows" as rows, "services"."status" as services_status, "fax_services"."status" as fax_services_status
	FROM "fax_services", "services", "lists"
	WHERE "services"."service_type" = 'FAX'
	AND "services"."member_id" = '7'
	AND "services"."id" = fax_services.service_id
	AND "services"."status" != 8
	ORDER BY "fax_services"."demand_date" desc
	LIMIT 20
2014-01-10 10:48:54 ICT netreal-netreal_test LOG:  期間: 0.803 ミリ秒
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  期間: 0.152 ミリ秒
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  期間: 0.187 ミリ秒
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  期間: 0.719 ミリ秒
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  期間: 0.147 ミリ秒
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  文: SELECT "tel_services"."send_date_start" as send_date_start, "services"."title" as title, "tel_services"."call_ok" as call_ok, "tel_services"."call_ng" as call_ng, "lists"."rows" as rows, "services"."status" as services_status, "tel_services"."status" as tel_services_status
	FROM "tel_services", "services", "lists"
	WHERE "services"."service_type" = 'TEL'
	AND "services"."member_id" = '7'
	AND "services"."id" = tel_services.service_id
	AND "services"."status" != 8
	ORDER BY "tel_services"."send_date_start" desc
	LIMIT 20
2014-01-10 10:48:55 ICT netreal-netreal_test LOG:  期間: 0.403 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.009 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.186 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.000 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.604 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT "dm_services"."demand_date" as demand_date, "services"."title" as title, "lists"."rows" as rows, "services"."status" as services_status, "dm_services"."status" as dm_services_status
	FROM "dm_services", "services", "lists"
	WHERE "services"."service_type" = 'DM'
	AND "services"."member_id" = '7'
	AND "services"."id" = dm_services.service_id
	AND "services"."status" != 8
	ORDER BY "dm_services"."demand_date" desc
	LIMIT 20
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.336 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.105 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.186 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.257 ミリ秒
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  文: SELECT "pdm_services"."demand_date" as demand_date, "services"."title" as title, "lists"."rows" as rows, "services"."status" as services_status, "pdm_services"."status" as pdm_services_status
	FROM "pdm_services", "services", "lists"
	WHERE "services"."service_type" = 'PDM'
	AND "services"."member_id" = '7'
	AND "services"."id" = pdm_services.service_id
	AND "services"."status" != 8
	ORDER BY "pdm_services"."demand_date" desc
	LIMIT 20
2014-01-10 10:48:57 ICT netreal-netreal_test LOG:  期間: 0.247 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.161 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.192 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: DELETE FROM "ci_sessions"
	WHERE "last_activity" < 1389311340
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.100 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = 'a:1:{s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.749 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = '' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.601 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325727', "user_data" = '' WHERE "session_id" = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.318 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.088 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: INSERT INTO "ci_sessions" ("session_id", "ip_address", "user_agent", "last_activity") VALUES ('b43bf6e7b47d6e35cf6513ba73a32b5b', '118.70.171.28', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26', 1389325740)
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 2.527 ミリ秒
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  文: DELETE FROM "ci_sessions"
	WHERE "last_activity" < 1389318540
2014-01-10 10:49:00 ICT netreal-netreal_test LOG:  期間: 0.116 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.082 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.197 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-07 10:49:07')
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.000 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.114 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.153 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.137 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = '' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.485 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.456 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.314 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.080 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.355 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.138 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.001 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.194 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'b43bf6e7b47d6e35cf6513ba73a32b5b' 
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.126 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.189 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.320 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT
	                                    *
	                                FROM
	                                    ci_sessions
	                                WHERE
	                                    ci_sessions.session_id = 'a51e1876b11338c4f21350899b019626'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.107 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: UPDATE
	                                    ci_sessions
	                                        SET
	                                            ip_address    = '118.70.171.28' ,
	                                            user_agent    = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26' ,
	                                            last_activity = '1389325727' ,
	                                            user_data     = ''
	                                        WHERE
	                                            session_id  = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.897 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.289 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.238 ミリ秒
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:49:07 ICT netreal-netreal_test LOG:  期間: 0.152 ミリ秒
2014-01-10 10:49:09 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:09 ICT netreal-netreal_test LOG:  期間: 0.151 ミリ秒
2014-01-10 10:49:09 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:09 ICT netreal-netreal_test LOG:  期間: 0.190 ミリ秒
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  期間: 0.079 ミリ秒
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  期間: 0.187 ミリ秒
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:11 ICT netreal-netreal_test LOG:  期間: 0.412 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.077 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.213 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-16 10:49:16')
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.348 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.120 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.163 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.000 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = '' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.749 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.667 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.771 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.089 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.539 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.227 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.194 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = 'b43bf6e7b47d6e35cf6513ba73a32b5b' 
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.110 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.089 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.185 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.171 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.134 ミリ秒
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:49:16 ICT netreal-netreal_test LOG:  期間: 0.109 ミリ秒
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  期間: 0.080 ミリ秒
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  期間: 0.186 ミリ秒
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  期間: 0.194 ミリ秒
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  期間: 0.123 ミリ秒
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  文: SELECT "dm_services"."demand_date" as demand_date, "services"."title" as title, "lists"."rows" as rows, "services"."status" as services_status, "dm_services"."status" as dm_services_status
	FROM "dm_services", "services", "lists"
	WHERE "services"."service_type" = 'DM'
	AND "services"."member_id" = '7'
	AND "services"."id" = dm_services.service_id
	AND "services"."status" != 8
	ORDER BY "dm_services"."demand_date" desc
	LIMIT 20
2014-01-10 10:49:18 ICT netreal-netreal_test LOG:  期間: 0.322 ミリ秒
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  期間: 0.398 ミリ秒
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  期間: 0.296 ミリ秒
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  期間: 0.179 ミリ秒
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  期間: 0.121 ミリ秒
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  文: SELECT "tel_services"."send_date_start" as send_date_start, "services"."title" as title, "tel_services"."call_ok" as call_ok, "tel_services"."call_ng" as call_ng, "lists"."rows" as rows, "services"."status" as services_status, "tel_services"."status" as tel_services_status
	FROM "tel_services", "services", "lists"
	WHERE "services"."service_type" = 'TEL'
	AND "services"."member_id" = '7'
	AND "services"."id" = tel_services.service_id
	AND "services"."status" != 8
	ORDER BY "tel_services"."send_date_start" desc
	LIMIT 20
2014-01-10 10:49:19 ICT netreal-netreal_test LOG:  期間: 0.280 ミリ秒
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  期間: 0.265 ミリ秒
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  期間: 0.303 ミリ秒
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  期間: 0.142 ミリ秒
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  期間: 0.147 ミリ秒
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  文: SELECT "fax_services"."demand_date" as demand_date, "services"."title" as title, "fax_services"."send_ok" as send_ok, "fax_services"."send_ng" as send_ng, "lists"."rows" as rows, "services"."status" as services_status, "fax_services"."status" as fax_services_status
	FROM "fax_services", "services", "lists"
	WHERE "services"."service_type" = 'FAX'
	AND "services"."member_id" = '7'
	AND "services"."id" = fax_services.service_id
	AND "services"."status" != 8
	ORDER BY "fax_services"."demand_date" desc
	LIMIT 20
2014-01-10 10:49:20 ICT netreal-netreal_test LOG:  期間: 0.310 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.170 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.280 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = 'a:1:{s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 1.413 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = '' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.357 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325740', "user_data" = '' WHERE "session_id" = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.307 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.190 ミリ秒
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  文: INSERT INTO "ci_sessions" ("session_id", "ip_address", "user_agent", "last_activity") VALUES ('0942733ca7e7e172e6177dfe9858746a', '118.70.171.28', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26', 1389325763)
2014-01-10 10:49:23 ICT netreal-netreal_test LOG:  期間: 0.944 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.238 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.283 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT 
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
	                                (end_date IS NULL OR TO_CHAR(end_date , 'yyyy-mm-dd hh24:mi:ss') >= '2014-01-29 10:49:29')
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.729 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT COUNT(*) AS cnt FROM members WHERE login_id = 'NF105001' AND login_pass = 'FdmdrjZL'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.136 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT 
	                        members.* 
	                    FROM 
	                        members  WHERE  members.login_id = 'NF105001'
	                    ORDER BY 
	                        members.regist_date DESC
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.186 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT 
	                            end_date
	                        FROM 
	                            partner_ticket_members 
	                                LEFT JOIN 
	                                    partner_tickets ON 
	                                        partner_ticket_members.partner_tickets_id = partner_tickets.id 
	                        WHERE 
	                            login_id    = 'NF105001'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.145 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325763', "user_data" = '' WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 2.439 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325763', "user_data" = 'a:1:{s:9:"member_id";s:1:"7";}' WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.624 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325763', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.442 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT price FROM fax_price_master_dear_member WHERE member_id = 7
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.093 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: UPDATE "ci_sessions" SET "last_activity" = '1389325763', "user_data" = 'a:2:{s:9:"member_id";s:1:"7";s:8:"login_id";s:8:"NF105001";}' WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.567 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT 
	                                    *
	                                FROM 
	                                    agencies 
	                                        LEFT JOIN partners ON 
	                                            agencies.partner_id = partners.id 
	                                WHERE 
	                                    partners.affiliater_id ='1'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.207 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.270 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.289 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT 
	                        * 
	                    FROM 
	                        ci_sessions  WHERE  ci_sessions.session_id = '0942733ca7e7e172e6177dfe9858746a' 
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.105 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.249 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.285 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT
	                                    *
	                                FROM
	                                    ci_sessions
	                                WHERE
	                                    ci_sessions.session_id = 'b43bf6e7b47d6e35cf6513ba73a32b5b'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.106 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: UPDATE
	                                    ci_sessions
	                                        SET
	                                            ip_address    = '118.70.171.28' ,
	                                            user_agent    = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26' ,
	                                            last_activity = '1389325740' ,
	                                            user_data     = ''
	                                        WHERE
	                                            session_id  = '0942733ca7e7e172e6177dfe9858746a'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.566 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "members"
	WHERE "id" = '7'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.140 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "news"
	WHERE "member_main" = 't'
	AND "show_flg" = 't'
	ORDER BY "regist_date" desc
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.120 ミリ秒
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "lists"
	WHERE "member_id" = '7'
	AND "list_type" = 'FAX'
	AND "test_flg" = 't'
2014-01-10 10:49:29 ICT netreal-netreal_test LOG:  期間: 0.112 ミリ秒
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  期間: 0.385 ミリ秒
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  期間: 0.189 ミリ秒
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  文: RESET ALL;
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  期間: 0.078 ミリ秒
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  文: SELECT *
	FROM "ci_sessions"
	WHERE "session_id" = '0942733ca7e7e172e6177dfe9858746a'
	AND "user_agent" = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26'
2014-01-10 10:49:31 ICT netreal-netreal_test LOG:  期間: 0.209 ミリ秒