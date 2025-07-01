--
-- PostgreSQL database dump
--

-- Dumped from database version 13.21 (Debian 13.21-1.pgdg120+1)
-- Dumped by pg_dump version 14.12 (Homebrew)

-- Started on 2025-07-01 11:02:20 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 639 (class 1247 OID 16410)
-- Name: account_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.account_type AS ENUM (
    'SAVINGS',
    'CREDIT_CARD'
);


ALTER TYPE public.account_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 204 (class 1259 OID 16417)
-- Name: accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accounts (
    account_id bigint NOT NULL,
    user_id bigint NOT NULL,
    account_name character varying(255) NOT NULL,
    account_type character varying(255) NOT NULL,
    balance numeric(38,2) DEFAULT 0.00,
    credit_limit numeric(38,2),
    statement_date integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.accounts OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16415)
-- Name: accounts_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.accounts_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.accounts_account_id_seq OWNER TO postgres;

--
-- TOC entry 3078 (class 0 OID 0)
-- Dependencies: 203
-- Name: accounts_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.accounts_account_id_seq OWNED BY public.accounts.account_id;


--
-- TOC entry 208 (class 1259 OID 16457)
-- Name: expense_splits; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expense_splits (
    split_id bigint NOT NULL,
    expense_id bigint NOT NULL,
    user_id bigint NOT NULL,
    owes_amount numeric(38,2) NOT NULL,
    is_settled boolean DEFAULT false,
    created_at date DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.expense_splits OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 16455)
-- Name: expense_splits_split_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.expense_splits_split_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.expense_splits_split_id_seq OWNER TO postgres;

--
-- TOC entry 3079 (class 0 OID 0)
-- Dependencies: 207
-- Name: expense_splits_split_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.expense_splits_split_id_seq OWNED BY public.expense_splits.split_id;


--
-- TOC entry 206 (class 1259 OID 16435)
-- Name: expenses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expenses (
    expense_id bigint NOT NULL,
    description character varying(255) NOT NULL,
    amount numeric(38,2) NOT NULL,
    expense_date date NOT NULL,
    paid_by_user_id bigint NOT NULL,
    account_id bigint,
    category character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.expenses OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16433)
-- Name: expenses_expense_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.expenses_expense_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.expenses_expense_id_seq OWNER TO postgres;

--
-- TOC entry 3080 (class 0 OID 0)
-- Dependencies: 205
-- Name: expenses_expense_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.expenses_expense_id_seq OWNED BY public.expenses.expense_id;


--
-- TOC entry 200 (class 1259 OID 16385)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16397)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id bigint NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16395)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 3081 (class 0 OID 0)
-- Dependencies: 201
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 2909 (class 2604 OID 16478)
-- Name: accounts account_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts ALTER COLUMN account_id SET DEFAULT nextval('public.accounts_account_id_seq'::regclass);


--
-- TOC entry 2914 (class 2604 OID 16527)
-- Name: expense_splits split_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_splits ALTER COLUMN split_id SET DEFAULT nextval('public.expense_splits_split_id_seq'::regclass);


--
-- TOC entry 2912 (class 2604 OID 16567)
-- Name: expenses expense_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses ALTER COLUMN expense_id SET DEFAULT nextval('public.expenses_expense_id_seq'::regclass);


--
-- TOC entry 2907 (class 2604 OID 16613)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 3068 (class 0 OID 16417)
-- Dependencies: 204
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.accounts (account_id, user_id, account_name, account_type, balance, credit_limit, statement_date, created_at) FROM stdin;
3	2	Checking Account	SAVINGS	3000.00	\N	\N	2025-06-25 11:46:49.218138
4	2	Another Credit Card	CREDIT_CARD	275.00	10000.00	2	2025-06-25 11:46:49.218138
5	1	Axis	CREDIT_CARD	200.00	50000.00	15	2025-06-25 11:46:49.218138
1	1	Savings Account	SAVINGS	2600.00	\N	\N	2025-06-25 11:46:49.218138
2	1	MILLENIA	CREDIT_CARD	1605.00	5000.00	15	2025-06-25 11:46:49.218138
\.


--
-- TOC entry 3072 (class 0 OID 16457)
-- Dependencies: 208
-- Data for Name: expense_splits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expense_splits (split_id, expense_id, user_id, owes_amount, is_settled, created_at) FROM stdin;
1	4	2	25.00	t	2025-06-25
2	12	2	25.00	t	2025-06-27
4	19	2	25.00	f	2025-07-01
7	17	2	200.00	f	\N
8	15	1	40.00	f	\N
\.


--
-- TOC entry 3070 (class 0 OID 16435)
-- Dependencies: 206
-- Data for Name: expenses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.expenses (expense_id, description, amount, expense_date, paid_by_user_id, account_id, category, created_at) FROM stdin;
4	Food	50.00	2025-06-25	1	1	Food	2025-06-25 11:48:29.639636
5	Entertainment	2000.00	2025-06-26	1	1	Entertainment	2025-06-26 09:16:39.351822
6	Bills	500.00	2025-06-26	2	3	Bills	2025-06-26 09:27:10.120061
7	Other	600.00	2025-06-26	1	1	Other	2025-06-26 09:27:53.41446
8	Food	200.00	2025-06-26	1	2	Food	2025-06-26 09:29:35.261493
9	Bills	1200.00	2025-06-26	1	1	Bills	2025-06-26 09:32:10.520848
10	Other	850.00	2025-06-27	1	1	Other	2025-06-27 07:41:24.304921
11	Food	800.00	2025-06-27	1	2	Food	2025-06-27 07:41:44.854843
12	Entertainment	100.00	2025-06-27	1	2	Entertainment	2025-06-27 07:43:24.530488
13	Food	1000.00	2025-06-27	1	2	Food	2025-06-27 07:44:08.336943
14	Entertainment	2000.00	2025-06-27	1	2	Entertainment	2025-06-27 07:44:44.084939
16	Settle	25.00	2025-07-01	2	4	Settle	2025-07-01 08:45:21.392726
18	Settle	250.00	2025-07-01	2	4	Settle	2025-07-01 08:47:05.740506
19	Other	55.00	2025-07-01	1	2	Other	2025-07-01 08:57:10.784164
20	Credit Card Statement Payment - MILLENIA	500.00	2025-07-01	1	1	statement	2025-07-01 09:47:49.649286
21	Credit Card Statement Payment - MILLENIA	400.00	2025-07-01	1	1	statement	2025-07-01 10:16:54.625436
22	Credit Card Statement Payment - Axis	200.00	2025-07-01	1	1	statement	2025-07-01 10:23:33.784188
23	Credit Card Statement Payment - MILLENIA	300.00	2025-07-01	1	1	statement	2025-07-01 10:23:48.44149
17	Food	400.00	2025-07-01	1	2	Food	2025-07-01 08:46:33.636844
15	Settle	50.00	2025-07-01	2	3	Food	2025-07-01 08:39:18.083659
\.


--
-- TOC entry 3064 (class 0 OID 16385)
-- Dependencies: 200
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	create tables	SQL	V1__create_tables.sql	1239066029	postgres	2025-06-25 11:46:49.128771	44	t
2	2	insert seed data	SQL	V2__insert_seed_data.sql	368470376	postgres	2025-06-25 11:46:49.196116	5	t
3	3	insert account seed data	SQL	V3__insert_account_seed_data.sql	-991462709	postgres	2025-06-25 11:46:49.213475	6	t
4	4	insert expense seed data	SQL	V4__insert_expense_seed_data.sql	-1252341575	postgres	2025-06-25 11:46:49.230682	3	t
\.


--
-- TOC entry 3066 (class 0 OID 16397)
-- Dependencies: 202
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username, password, created_at) FROM stdin;
2	user2	password	2025-06-25 11:46:49.200991
1	Test user	password	2025-06-25 11:46:49.200991
\.


--
-- TOC entry 3082 (class 0 OID 0)
-- Dependencies: 203
-- Name: accounts_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.accounts_account_id_seq', 5, true);


--
-- TOC entry 3083 (class 0 OID 0)
-- Dependencies: 207
-- Name: expense_splits_split_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.expense_splits_split_id_seq', 8, true);


--
-- TOC entry 3084 (class 0 OID 0)
-- Dependencies: 205
-- Name: expenses_expense_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.expenses_expense_id_seq', 23, true);


--
-- TOC entry 3085 (class 0 OID 0)
-- Dependencies: 201
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 2, true);


--
-- TOC entry 2924 (class 2606 OID 16480)
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (account_id);


--
-- TOC entry 2928 (class 2606 OID 16529)
-- Name: expense_splits expense_splits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_splits
    ADD CONSTRAINT expense_splits_pkey PRIMARY KEY (split_id);


--
-- TOC entry 2926 (class 2606 OID 16569)
-- Name: expenses expenses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_pkey PRIMARY KEY (expense_id);


--
-- TOC entry 2917 (class 2606 OID 16393)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 2920 (class 2606 OID 16615)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 2922 (class 2606 OID 16408)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 2918 (class 1259 OID 16394)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 2929 (class 2606 OID 16616)
-- Name: accounts accounts_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 2932 (class 2606 OID 16570)
-- Name: expense_splits expense_splits_expense_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_splits
    ADD CONSTRAINT expense_splits_expense_id_fkey FOREIGN KEY (expense_id) REFERENCES public.expenses(expense_id);


--
-- TOC entry 2933 (class 2606 OID 16621)
-- Name: expense_splits expense_splits_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expense_splits
    ADD CONSTRAINT expense_splits_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 2930 (class 2606 OID 16589)
-- Name: expenses expenses_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_account_id_fkey FOREIGN KEY (account_id) REFERENCES public.accounts(account_id);


--
-- TOC entry 2931 (class 2606 OID 16626)
-- Name: expenses expenses_paid_by_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_paid_by_user_id_fkey FOREIGN KEY (paid_by_user_id) REFERENCES public.users(user_id);


-- Completed on 2025-07-01 11:02:20 IST

--
-- PostgreSQL database dump complete
--

