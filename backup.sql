--
-- PostgreSQL database dump
--

-- Dumped from database version 15.7
-- Dumped by pg_dump version 16.3

-- Started on 2025-03-23 10:45:58

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 17373)
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    post_id character varying(255) NOT NULL,
    content character varying(255),
    status integer,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone DEFAULT now(),
    deleted boolean,
    likes integer,
    reply jsonb
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17465)
-- Name: conversation_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conversation_participants (
    id character varying(255) NOT NULL,
    conversation_id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    left_at timestamp without time zone
);


ALTER TABLE public.conversation_participants OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17453)
-- Name: conversations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conversations (
    id character varying(255) NOT NULL,
    type character varying(50) NOT NULL,
    name character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT conversations_type_check CHECK (((type)::text = ANY ((ARRAY['PRIVATE'::character varying, 'GROUP'::character varying])::text[])))
);


ALTER TABLE public.conversations OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 17321)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 17326)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17352)
-- Name: friendships; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.friendships (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    friend_id character varying(255) NOT NULL,
    status integer,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone
);


ALTER TABLE public.friendships OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17396)
-- Name: group_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.group_members (
    id character varying(255) NOT NULL,
    group_id character varying(255),
    user_id character varying(255),
    role character varying(50) DEFAULT 'member'::character varying,
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status integer,
    CONSTRAINT group_members_role_check CHECK (((role)::text = ANY ((ARRAY['member'::character varying, 'admin'::character varying, 'moderator'::character varying])::text[])))
);


ALTER TABLE public.group_members OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17387)
-- Name: groups; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.groups (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    profile_image character varying(255),
    cover_image character varying(255),
    privacy_status integer,
    created_by character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean
);


ALTER TABLE public.groups OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17364)
-- Name: likes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.likes (
    id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    post_id character varying(255) NOT NULL,
    status integer,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone
);


ALTER TABLE public.likes OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17476)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    id character varying(255) NOT NULL,
    conversation_id character varying(255) NOT NULL,
    sender_id character varying(255) NOT NULL,
    content text NOT NULL,
    attachments jsonb,
    is_read boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17432)
-- Name: notification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification (
    id character varying(255) NOT NULL,
    receive_id character varying(255) NOT NULL,
    sender_id character varying(255),
    post_id character varying(255),
    type character varying(50) NOT NULL,
    message text NOT NULL,
    data jsonb DEFAULT '{}'::jsonb,
    is_read boolean DEFAULT false,
    created_at timestamp with time zone DEFAULT now()
);


ALTER TABLE public.notification OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17415)
-- Name: page_followers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.page_followers (
    id character varying(255) NOT NULL,
    page_id character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    page_role character varying(255),
    status integer
);


ALTER TABLE public.page_followers OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17406)
-- Name: pages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pages (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    profile_image character varying(255),
    cover_image character varying(255),
    category character varying(255),
    created_by character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    deleted boolean,
    regime integer
);


ALTER TABLE public.pages OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17344)
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
    post_id character varying(255),
    user_id character varying(255),
    content text NOT NULL,
    image_url jsonb,
    status integer,
    deleted boolean,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    tag jsonb,
    hashtag jsonb,
    likes integer,
    comments integer,
    type character varying(50),
    share_info jsonb,
    privacy integer,
    type_file character varying(255)
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17442)
-- Name: stories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stories (
    id character varying(255) NOT NULL,
    user_id character varying(50),
    content character varying(255),
    video_url character varying(255),
    background_color character varying(20),
    is_active boolean DEFAULT true,
    type character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    expires_at timestamp without time zone
);


ALTER TABLE public.stories OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17331)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id character varying(255) NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    age integer,
    full_name character varying(100),
    avatar_url character varying(255),
    education_level character varying(255),
    gender integer,
    status integer NOT NULL,
    address character varying(255),
    phone_number character varying(255),
    description character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    access_token_reset_at timestamp without time zone,
    failed_login_count integer,
    date_of_birth date,
    list_images jsonb,
    count_likes integer,
    count_follows integer
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3453 (class 0 OID 17373)
-- Dependencies: 220
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, user_id, post_id, content, status, created_at, updated_at, deleted, likes, reply) FROM stdin;
25069162538955527692354253490645	25052230727639666685406250059829	25066091304658516345385365729210	aaaaaaaaaaaaaaaaaaaaaaaaaa	1	2025-03-10 16:25:38.948487	2025-03-10 16:37:15.006928	f	0	[{"id": "d6a8cd60-9cf8-4545-929c-1ce6971b9856", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "vvvvvvvvvvvvvvvvvvvvvv", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T16:25:42.6803232"}, {"id": "24f4a340-7ad8-4cfa-85a8-9496007b2eab", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "cccccccccccccccccccccccc", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T16:25:49.9333059"}, {"id": "bdf74224-9014-4fcc-8ee9-d9aba4091a23", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "fafafasfaf", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T16:34:32.4295518"}, {"id": "dfb3170a-e992-4ad2-914d-acab63625f7c", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "aaaaaaaaaaaaaaaaaaaaaaaaaadasdaaa", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T16:37:15.0058043"}]
25069163742906176895794999821607	25052230727639666685406250059829	25066091304658516345385365729210	bbbbbbbbbbbbbbbbbbbbbbbbbbbbb	1	2025-03-10 16:37:42.899473	2025-03-10 16:37:42.899473	f	0	\N
25069163747583237417376770268243	25052230727639666685406250059829	25066091304658516345385365729210	ccccccccccccccccccccccccccccc	1	2025-03-10 16:37:47.583521	2025-03-10 16:37:47.583521	f	0	\N
25069163800778718618838148467544	25052230727639666685406250059829	25066091304658516345385365729210	aaaaaaaaaaaaaaaaaaaaaadddddddbbbbbbbbbbbbbbbbbbbbbbccc	1	2025-03-10 16:38:00.778558	2025-03-10 16:46:10.20483	f	0	[{"id": "a19c8c3c-c11c-424d-8894-5adddc6e4d41", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "ádasdasdas", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": [2025, 3, 10, 16, 46, 10, 190988000]}]
25069165918786723541363006755160	25052230727639666685406250059829	25066091304658516345385365729210	ao vcl 	1	2025-03-10 16:59:18.786981	2025-03-10 16:59:18.786981	f	0	\N
25069170054224348180173902515218	25052230727639666685406250059829	25066091304658516345385365729210	eo tin 	1	2025-03-10 17:00:54.224431	2025-03-10 17:02:03.963443	f	0	[{"id": "38d92fb7-5c20-4fef-ada4-627bdc67ed77", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "o kia ", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": [2025, 3, 10, 17, 2, 3, 948586000]}]
25069170039223564808645557154902	25052230727639666685406250059829	25066091304658516345385365729210	cccc 	1	2025-03-10 17:00:39.223707	2025-03-10 19:38:12.201058	f	0	[{"id": "a9dedfe4-cdc6-4f2c-bb97-ec65ed848909", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "sao co ", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T17:02:18.4136906"}, {"id": "288c8bdb-e068-40c2-9313-d58a1774471e", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "khong co gi ", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T19:15:56.851062"}, {"id": "66edb200-1ce8-4279-a124-aa42a9a18c0c", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "oke", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T19:16:11.8317367"}, {"id": "4fe4da36-0e23-47af-b0c8-0da5fd27fbc7", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "ks", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T19:17:15.3703593"}, {"id": "a65b2304-c814-4fbe-85b3-4ea5bfd195c0", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "oke cau ", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T19:24:06.1454606"}, {"id": "c407cbb1-e259-45b2-bc22-2b07de1ac00a", "sender": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "content": "hi", "receiver": {"id": "25052230727639666685406250059829", "username": "xuanhieu", "avatarUrl": "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg"}, "createdAt": "2025-03-10T19:38:12.1920309"}]
\.


--
-- TOC entry 3461 (class 0 OID 17465)
-- Dependencies: 228
-- Data for Name: conversation_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.conversation_participants (id, conversation_id, user_id, joined_at, left_at) FROM stdin;
25080163003355235509993340286179	25080163003318404198659684134054	25055113438839619367078813101134	2025-03-21 16:30:03.3571	\N
25080163003359518440146264729323	25080163003318404198659684134054	25055113438839619367078813101134	2025-03-21 16:30:03.361122	\N
\.


--
-- TOC entry 3460 (class 0 OID 17453)
-- Dependencies: 227
-- Data for Name: conversations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.conversations (id, type, name, created_at, updated_at) FROM stdin;
25080163003318404198659684134054	PRIVATE	\N	2025-03-21 16:30:03.353843	2025-03-21 16:30:03.353843
\.


--
-- TOC entry 3447 (class 0 OID 17321)
-- Dependencies: 214
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
124	hieudx	db/changelog/changelog.sql	2025-03-04 09:58:29.627495	34	EXECUTED	8:3117d8bdb05f6b96bb4b572f28ddb178	sql		\N	4.20.0	\N	\N	1057109563
125	hieudx	db/changelog/changelog.sql	2025-03-05 14:50:52.889193	35	EXECUTED	8:beb811909e8e0c4a6ba33dff3d80d7ca	sql		\N	4.20.0	\N	\N	1161052831
126	hieudx	db/changelog/changelog.sql	2025-03-10 10:08:06.755923	36	EXECUTED	8:1d49ea317a83c3ec22b375bb763fba6f	sql		\N	4.20.0	\N	\N	1576086684
127	hieudx	db/changelog/changelog.sql	2025-03-10 10:08:06.780504	37	EXECUTED	8:10f3fc33487482ea2d5706ce5d4324d5	sql		\N	4.20.0	\N	\N	1576086684
128	hieudx	db/changelog/changelog.sql	2025-03-11 08:37:19.699075	38	EXECUTED	8:551e3a93f11b28baf7abad387f2d3937	sql		\N	4.20.0	\N	\N	1657039628
129	hieudx	db/changelog/changelog.sql	2025-03-16 22:19:30.45215	39	EXECUTED	8:f3f1c7f04d34970bf740e2ad04a094bd	sql		\N	4.20.0	\N	\N	2138370365
130	hieudx	db/changelog/changelog.sql	2025-03-21 14:31:52.582254	40	EXECUTED	8:9655a5b0be8f0bea675002d6246e5c4f	sql		\N	4.20.0	\N	\N	2542312494
131	hieudx	db/changelog/changelog.sql	2025-03-21 14:31:52.609425	41	EXECUTED	8:2b9db66f166cfcb48bdf78b3709b82dd	sql		\N	4.20.0	\N	\N	2542312494
7	hieudx	db/changelog/changelog.sql	2025-02-24 10:25:50.82322	7	EXECUTED	8:1fd12f11af5594b108e830fa8daead7a	sql		\N	4.27.0	\N	\N	0367550790
8	hieudx	db/changelog/changelog.sql	2025-02-24 11:42:32.52867	8	EXECUTED	8:5b958ba25c62c9f93c30a7af25f4920c	sql		\N	4.27.0	\N	\N	0372152505
6	hieudx	db/changelog/changelog.sql	2025-02-24 10:07:39.054137	6	EXECUTED	8:5c940c9a3776f4d7582d3a9096e13a0e	sql		\N	4.27.0	\N	\N	0366459003
3	hieudx	db/changelog/changelog.sql	2025-02-21 09:01:55.027738	3	EXECUTED	8:54810c616fc25458b0d14a0ce207352e	sql		\N	4.27.0	\N	\N	0103314988
5	hieudx	db/changelog/changelog.sql	2025-02-21 09:01:55.039278	5	EXECUTED	8:ab021fc14f09db6b4fc7434709d2c4a0	sql		\N	4.27.0	\N	\N	0103314988
1	hieudx	db/changelog/changelog.sql	2025-02-21 09:01:55.018779	1	EXECUTED	8:62ffb8d66c036bd5f19f9514049739a0	sql		\N	4.27.0	\N	\N	0103314988
2	hieudx	db/changelog/changelog.sql	2025-02-21 09:01:55.024429	2	EXECUTED	8:eb84e48917f0c6ec3858ab23d246888d	sql		\N	4.27.0	\N	\N	0103314988
4	hieudx	db/changelog/changelog.sql	2025-02-21 09:01:55.035457	4	EXECUTED	8:bcfe26aaf0e3ace21b19d70db4ea3c5b	sql		\N	4.27.0	\N	\N	0103314988
1	post	db/changelog/changelog.sql	2025-02-24 15:55:57.576022	9	EXECUTED	8:eec0b2ae8a1367eb4730950d57ebb19c	sql		\N	4.20.0	\N	\N	0387357528
100	hieudx	db/changelog/changelog.sql	2025-02-26 10:48:30.002507	10	EXECUTED	8:4d378356aeca8bfece67c3a37f485b20	sql		\N	4.20.0	\N	\N	0541709945
101	hieudx	db/changelog/changelog.sql	2025-02-26 10:52:03.043471	11	EXECUTED	8:9a15f6c48e965cc7fd3f585a43d08094	sql		\N	4.20.0	\N	\N	0541923010
102	hieudx	db/changelog/changelog.sql	2025-02-26 12:31:04.90034	12	EXECUTED	8:673b8fc8fa55a651ee8eb02c85f6063e	sql		\N	4.20.0	\N	\N	0547864823
103	hieudx	db/changelog/changelog.sql	2025-02-26 13:20:52.407915	13	EXECUTED	8:d1eb364e2602d04557614e03ec76b32c	sql		\N	4.20.0	\N	\N	0550852373
104	hieudx	db/changelog/changelog.sql	2025-02-26 15:19:38.684066	14	EXECUTED	8:10d78af47d50a8a29d379d7369a2fbd0	sql		\N	4.20.0	\N	\N	0557978623
105	hieudx	db/changelog/changelog.sql	2025-02-26 19:04:58.392056	15	EXECUTED	8:f3a342e2540a31521e687d5346d3f325	sql		\N	4.20.0	\N	\N	0571498339
106	hieudx	db/changelog/changelog.sql	2025-02-26 19:04:58.402392	16	EXECUTED	8:ae9d6bda55ae0669668780b748542180	sql		\N	4.20.0	\N	\N	0571498339
107	hieudx	db/changelog/changelog.sql	2025-02-26 19:13:27.76674	17	EXECUTED	8:bfe34778e82a0bcf2a3f9c63273fd3cb	sql		\N	4.20.0	\N	\N	0572007672
108	hieudx	db/changelog/changelog.sql	2025-02-26 22:45:38.971664	18	EXECUTED	8:c05e5e120f16a0f0d8c479f73e0814e6	sql		\N	4.20.0	\N	\N	0584738899
109	hieudx	db/changelog/changelog.sql	2025-02-26 22:46:07.380289	19	EXECUTED	8:30cb27aed4b8b190e253c7bcbd2cda51	sql		\N	4.20.0	\N	\N	0584767312
110	hieudx	db/changelog/changelog.sql	2025-02-26 22:46:07.401527	20	EXECUTED	8:b75c8d856741c4dd5ac6a2f9a10b49d0	sql		\N	4.20.0	\N	\N	0584767312
111	hieudx	db/changelog/changelog.sql	2025-02-26 22:46:33.295778	21	EXECUTED	8:7f2de0dd96f11b579bd9a96424cc33cf	sql		\N	4.20.0	\N	\N	0584793228
112	hieudx	db/changelog/changelog.sql	2025-02-26 22:46:33.322464	22	EXECUTED	8:49b7f16e1bce925c4c5405bedb8b79ac	sql		\N	4.20.0	\N	\N	0584793228
113	hieudx	db/changelog/changelog.sql	2025-02-26 22:46:33.337711	23	EXECUTED	8:0866e6aa04cb0cfd27ee78136fd172ae	sql		\N	4.20.0	\N	\N	0584793228
114	hieudx	db/changelog/changelog.sql	2025-02-27 08:25:31.290213	24	EXECUTED	8:d4bcf30dadf756498144b4173ad32f9d	sql		\N	4.20.0	\N	\N	0619531252
115	hieudx	db/changelog/changelog.sql	2025-02-27 08:25:31.297772	25	EXECUTED	8:4a449260a9a135422bd8f7332ea332c4	sql		\N	4.20.0	\N	\N	0619531252
116	hieudx	db/changelog/changelog.sql	2025-02-27 08:25:31.302047	26	EXECUTED	8:3ce83e7bb48041b5e719cae6bbd7c9da	sql		\N	4.20.0	\N	\N	0619531252
117	hieudx	db/changelog/changelog.sql	2025-02-27 08:49:19.713347	27	EXECUTED	8:7f9789546544567d4c3dd69693fb5e17	sql		\N	4.20.0	\N	\N	0620959590
118	hieudx	db/changelog/changelog.sql	2025-02-27 08:49:19.72315	28	EXECUTED	8:9da739649957d57ae63f39a06b37d055	sql		\N	4.20.0	\N	\N	0620959590
119	hieudx	db/changelog/changelog.sql	2025-02-27 08:49:19.728218	29	EXECUTED	8:2eb898b35c97529f7823526f98f2bbb9	sql		\N	4.20.0	\N	\N	0620959590
120	hieudx	db/changelog/changelog.sql	2025-02-27 11:27:32.616389	30	EXECUTED	8:db27e7379d83e125cee9380652dc71ab	sql		\N	4.20.0	\N	\N	0630452569
121	hieudx	db/changelog/changelog.sql	2025-02-28 11:18:57.792563	31	EXECUTED	8:f3a787fedaab9c6d5ded381ae6c4a282	sql		\N	4.20.0	\N	\N	0716337733
122	hieudx	db/changelog/changelog.sql	2025-03-02 17:10:31.79182	32	EXECUTED	8:46ae256faa10bf9c915500cde9c52b52	sql		\N	4.20.0	\N	\N	0910231726
123	hieudx	db/changelog/changelog.sql	2025-03-02 17:10:31.803217	33	EXECUTED	8:708d69b599298a5568847bc5f75fec01	sql		\N	4.20.0	\N	\N	0910231726
132	hieudx	db/changelog/changelog.sql	2025-03-21 14:31:52.627711	42	EXECUTED	8:9b7b33a90db80820631a8716c5d88488	sql		\N	4.20.0	\N	\N	2542312494
\.


--
-- TOC entry 3448 (class 0 OID 17326)
-- Dependencies: 215
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
\.


--
-- TOC entry 3451 (class 0 OID 17352)
-- Dependencies: 218
-- Data for Name: friendships; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friendships (id, user_id, friend_id, status, created_at, updated_at) FROM stdin;
25060005237098168785752972191084	25052230727639666685406250059829	25059224522572828172277451169875	3	2025-03-01 00:52:37.096634	2025-03-02 09:37:41.928274
25061111739234164090038635802879	25061111625457403196441300829178	25059234229788357447930868028765	2	2025-03-02 11:17:39.234789	2025-03-02 11:18:01.16759
25061111819850585003214778252342	25061111625457403196441300829178	25059234123604113011211455616547	1	2025-03-02 11:18:19.850849	2025-03-02 11:18:19.850849
25061111757192165532744798077589	25061111625457403196441300829178	25055134706490805496240886736318	1	2025-03-02 11:17:57.192339	2025-03-02 11:27:06.022058
25061111731909791812448396786326	25061111625457403196441300829178	25059234351734700096766708535158	4	2025-03-02 11:17:31.907112	2025-03-02 11:30:03.369112
25061113103661387523200096230798	25061111625457403196441300829178	25059224212795097341037050503575	3	2025-03-02 11:31:03.656756	2025-03-02 11:35:57.242074
25061170039358854049618140806570	25052230727639666685406250059829	25059224212795097341037050503575	2	2025-03-02 17:00:39.329477	2025-03-02 17:00:42.942245
25060005421866567999634950329362	25052230727639666685406250059829	25055133853118225902485283188890	2	2025-03-01 00:54:21.86674	2025-03-02 17:00:45.252547
25062152007691230377304546866725	25061111625457403196441300829178	25055133853118225902485283188890	4	2025-03-03 15:20:07.674078	2025-03-03 15:20:24.080552
25062153801747006954332372288625	25055113902427548222505933023551	25061111625457403196441300829178	1	2025-03-03 15:38:01.740986	2025-03-03 15:38:01.740986
25062155449978983368713006796160	25055113902427548222505933023551	25055133853118225902485283188890	1	2025-03-03 15:54:49.846678	2025-03-03 15:54:49.846678
25055113930444177171751765620479	25055113438839619367078813101134	25055113902427548222505933023551	2	2025-02-24 11:39:30.444717	2025-03-03 15:57:52.498575
25070104900188281469913721334642	25052230727639666685406250059829	25055134706490805496240886736318	1	2025-03-11 10:49:00.150574	2025-03-11 10:49:00.150574
25062145015787019348533746328692	25052230727639666685406250059829	25061111625457403196441300829178	3	2025-03-03 14:50:15.786289	2025-03-21 13:42:45.956941
25060011301159561325206293747398	25052230727639666685406250059829	25059234741996733039402702628011	3	2025-03-01 01:13:01.159579	2025-03-21 13:42:55.193193
25072134848006471579935871173396	25052230727639666685406250059829	25059234229788357447930868028765	3	2025-03-13 13:48:48.00627	2025-03-21 13:43:07.386195
25070134151662576073407267441691	25052230727639666685406250059829	25055113902427548222505933023551	1	2025-03-11 13:41:51.649071	2025-03-11 13:41:51.649071
25072134632373253576845568694322	25059234229788357447930868028765	25052230727639666685406250059829	3	2025-03-13 13:46:32.371263	2025-03-21 13:43:07.394988
25060010906696330063865812500267	25052230727639666685406250059829	25059223744436012323964224169985	1	2025-03-01 01:09:06.696886	2025-03-01 01:09:06.696886
25060011232724276340189805084690	25052230727639666685406250059829	25059234822041495869043745170436	1	2025-03-01 01:12:32.724387	2025-03-01 01:12:32.724387
25060011354117978145179228559998	25052230727639666685406250059829	25059234845208633954427897551865	1	2025-03-01 01:13:54.117573	2025-03-01 01:13:54.117573
25070135258144308497242407735229	25052230727639666685406250059829	25059234351734700096766708535158	1	2025-03-11 13:52:58.11708	2025-03-11 13:52:58.11708
25072154919746632754780353691546	25052230727639666685406250059829	25059234123604113011211455616547	1	2025-03-13 15:49:19.744657	2025-03-13 15:49:19.744657
25080100937418144095574000652234	25052230727639666685406250059829	25055113438839619367078813101134	1	2025-03-21 10:09:37.415124	2025-03-21 13:51:32.296272
25055113701944033513035444990506	25055113438839619367078813101134	25052230727639666685406250059829	2	2025-02-24 11:37:01.941913	2025-03-21 14:18:35.20049
25070110134281621231779035492668	25059234123604113011211455616547	25052230727639666685406250059829	4	2025-03-11 11:01:34.275177	2025-03-11 16:23:39.663926
\.


--
-- TOC entry 3455 (class 0 OID 17396)
-- Dependencies: 222
-- Data for Name: group_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.group_members (id, group_id, user_id, role, joined_at, status) FROM stdin;
25061212732431237443823276521115	25061212732377722964497582045565	25061111625457403196441300829178	admin	2025-03-02 21:27:32.429462	1
25061213539881300225558602034296	25061213539870365484394821420315	25061111625457403196441300829178	admin	2025-03-02 21:35:39.88181	1
25061213803961290424718044247944	25061213803950508923956722379579	25061111625457403196441300829178	admin	2025-03-02 21:38:03.961766	1
25061214041885361931421285767702	25061214041872230549540994190058	25061111625457403196441300829178	admin	2025-03-02 21:40:41.885414	1
25061223204276653070786749644014	25061223204182104308570809485425	25052230727639666685406250059829	admin	2025-03-02 22:32:04.276762	1
25061213930633269100956070500325	25061213930620745957198730120810	25061111625457403196441300829178	admin	2025-03-02 21:39:30.631467	1
25061214151153454150979333423693	25061214151150438948707772817018	25061111625457403196441300829178	admin	2025-03-02 21:41:51.15345	1
25062130217342966857534789905363	25061212732377722964497582045565	25052230727639666685406250059829	member	2025-03-03 13:02:16.945875	1
25062143408218103826349614925624	25061223204182104308570809485425	25061111625457403196441300829178	member	2025-03-03 14:34:08.201804	1
25062144739001384204260534448201	25061213539870365484394821420315	25052230727639666685406250059829	member	2025-03-03 14:47:38.99708	1
25062133615222242995166464432948	25061214041872230549540994190058	25052230727639666685406250059829	member	2025-03-03 13:36:15.207857	1
\.


--
-- TOC entry 3454 (class 0 OID 17387)
-- Dependencies: 221
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.groups (id, name, description, profile_image, cover_image, privacy_status, created_by, created_at, updated_at, deleted) FROM stdin;
25061213539870365484394821420315	GROUP B	bbbbbbbbbbbbbbbbbbbbbbbbbbbb	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926140/vwwxbouvisv06yxgkqez.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926138/rqjlapngboqofzu83uag.jpg	1	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061214151150438948707772817018	GROUP H	hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926511/pyqtyoer3rxqxx12vv23.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926509/xmjpffu1ziyvn1gcndt0.jpg	0	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061213803950508923956722379579	GROUP C	cccccccccccccccccccccccccccccc	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926284/vfmxdqvseca8dwnndc5j.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926282/g26khd836mhqwsdcqzg2.jpg	1	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061213930620745957198730120810	GROUP D	dddddddddddddddddddddddddddddd	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926371/hbmacn6ezno5ruvgdanv.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926368/rpmb5dt0jl2jl1dwyqk6.jpg	0	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061214041872230549540994190058	GROUP E	eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926442/bgdsr1pjcndgsuubcmf4.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740926440/v0vzyybjt9dbgksy6i9k.jpg	0	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061212732377722964497582045565	GROUP A	aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740925653/lggl4purvulahslfqcl0.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740925650/rtoi096sh3jsoxol0z5u.jpg	1	25061111625457403196441300829178	2025-03-02 21:42:13	2025-03-02 21:42:13	f
25061223204182104308570809485425	GROUP ABC 	abcdefghjklmnbvcx	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740929524/fini1ghufudr6ezys1jx.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740929523/jkzf33ciku4adjxzlfph.jpg	0	25052230727639666685406250059829	2025-03-02 22:31:58.058133	2025-03-02 22:31:58.058133	f
\.


--
-- TOC entry 3452 (class 0 OID 17364)
-- Dependencies: 219
-- Data for Name: likes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.likes (id, user_id, post_id, status, created_at, updated_at) FROM stdin;
25057140430666855932703673056633	25055113438839619367078813101134	25057114534308843127568639349670	1	\N	\N
25057210848234958516946966278109	25055113438839619367078813101134	25057195223536214449676777652596	1	2025-02-26 21:08:48.230238	2025-02-26 21:09:11.861314
25069082812414107470147808777295	25052230727639666685406250059829	25066090742252820313101519604343	0	2025-03-10 08:28:12.406689	2025-03-10 08:28:13.496766
25070083026071464856767838278103	25052230727639666685406250059829	25066091059535792670709816043812	1	2025-03-11 08:30:26.060752	2025-03-11 08:30:30.633924
25066092040969010655358735852951	25052230727639666685406250059829	25066091304658516345385365729210	1	2025-03-07 09:20:40.955996	2025-03-14 09:12:08.407625
\.


--
-- TOC entry 3462 (class 0 OID 17476)
-- Dependencies: 229
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (id, conversation_id, sender_id, content, attachments, is_read, created_at) FROM stdin;
\.


--
-- TOC entry 3458 (class 0 OID 17432)
-- Dependencies: 225
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notification (id, receive_id, sender_id, post_id, type, message, data, is_read, created_at) FROM stdin;
25073085508579796772656963761022	25055113902427548222505933023551	25055113438839619367078813101134	25073085508560944501417325209663	NEW_POST	Dang Xuan Hieu vừa đăng bài viết mới	\N	f	2025-03-14 08:55:08.57065+07
25073085508586397844237802383018	25052230727639666685406250059829	25055113438839619367078813101134	25073085508560944501417325209663	NEW_POST	Dang Xuan Hieu vừa đăng bài viết mới	\N	f	2025-03-14 08:55:08.580632+07
25073093629140122062746595197262	25052230727639666685406250059829	25055113438839619367078813101134	\N	FRIEND_REQUEST	Bạn nhận được lời mời kết bạn.	\N	f	2025-03-14 09:36:29.127163+07
25076151020056227670157590721754	25059224212795097341037050503575	25052230727639666685406250059829	25076151019958467852545479874147	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-17 15:10:20.056131+07
25076151020072775244518550651577	25055133853118225902485283188890	25052230727639666685406250059829	25076151019958467852545479874147	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-17 15:10:20.072127+07
25076151020072569127096314948533	25055113438839619367078813101134	25052230727639666685406250059829	25076151019958467852545479874147	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-17 15:10:20.072127+07
25076151020072964262505431037684	25059234741996733039402702628011	25052230727639666685406250059829	25076151019958467852545479874147	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-17 15:10:20.072127+07
25076151020072626179517526548811	25061111625457403196441300829178	25052230727639666685406250059829	25076151019958467852545479874147	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-17 15:10:20.072127+07
25079085442148643649778737010643	25059224212795097341037050503575	25052230727639666685406250059829	25079085442098654487966345311167	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:42.148295+07
25079085442160431386700562594506	25055133853118225902485283188890	25052230727639666685406250059829	25079085442098654487966345311167	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:42.158355+07
25079085442162389939315300077143	25055113438839619367078813101134	25052230727639666685406250059829	25079085442098654487966345311167	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:42.162376+07
25079085442164102314877858658005	25059234741996733039402702628011	25052230727639666685406250059829	25079085442098654487966345311167	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:42.164389+07
25079085442168377833737012907737	25061111625457403196441300829178	25052230727639666685406250059829	25079085442098654487966345311167	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:42.168409+07
25079085448824265339278853506997	25059224212795097341037050503575	25052230727639666685406250059829	25079085448798137714061296527691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:48.82321+07
25079085448830762617965765858547	25055133853118225902485283188890	25052230727639666685406250059829	25079085448798137714061296527691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:48.830563+07
25079085448830425975593993742260	25055113438839619367078813101134	25052230727639666685406250059829	25079085448798137714061296527691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:48.830563+07
25079085448846923240960403471467	25059234741996733039402702628011	25052230727639666685406250059829	25079085448798137714061296527691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:48.842539+07
25079085448851746526644172347769	25061111625457403196441300829178	25052230727639666685406250059829	25079085448798137714061296527691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 08:54:48.851958+07
25079113810641004582745453935370	25059224212795097341037050503575	25052230727639666685406250059829	25079113810568645644937681521958	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:38:10.64162+07
25079113810655244246358616981034	25055133853118225902485283188890	25052230727639666685406250059829	25079113810568645644937681521958	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:38:10.655431+07
25079113810655595718478922372913	25055113438839619367078813101134	25052230727639666685406250059829	25079113810568645644937681521958	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:38:10.655431+07
25079113810661795188528417498073	25059234741996733039402702628011	25052230727639666685406250059829	25079113810568645644937681521958	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:38:10.661414+07
25079113810662243065570288702766	25061111625457403196441300829178	25052230727639666685406250059829	25079113810568645644937681521958	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:38:10.662836+07
25079114126375232667855083274377	25059224212795097341037050503575	25052230727639666685406250059829	25079114126359052191734595200754	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:41:26.37506+07
25079114126375581674670871113562	25055133853118225902485283188890	25052230727639666685406250059829	25079114126359052191734595200754	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:41:26.37506+07
25079114126390179157185527944079	25055113438839619367078813101134	25052230727639666685406250059829	25079114126359052191734595200754	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:41:26.390965+07
25079114126397631031032656235569	25059234741996733039402702628011	25052230727639666685406250059829	25079114126359052191734595200754	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:41:26.397994+07
25079114126406047396076188616427	25061111625457403196441300829178	25052230727639666685406250059829	25079114126359052191734595200754	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:41:26.397994+07
25079114248079734392652621263633	25059224212795097341037050503575	25052230727639666685406250059829	25079114248065640832476526427834	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:42:48.079224+07
25079114248094916156859610691276	25055133853118225902485283188890	25052230727639666685406250059829	25079114248065640832476526427834	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:42:48.094991+07
25079114248094253713442357308500	25055113438839619367078813101134	25052230727639666685406250059829	25079114248065640832476526427834	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:42:48.094991+07
25079114248094065850338093741999	25059234741996733039402702628011	25052230727639666685406250059829	25079114248065640832476526427834	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:42:48.094991+07
25079114248110377875855072277873	25061111625457403196441300829178	25052230727639666685406250059829	25079114248065640832476526427834	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:42:48.110974+07
25079114423886309969697414119153	25059224212795097341037050503575	25052230727639666685406250059829	25079114423870495712236143223769	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:44:23.886903+07
25079114423902411998992599750229	25055133853118225902485283188890	25052230727639666685406250059829	25079114423870495712236143223769	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:44:23.902671+07
25079114423902014665849462519016	25055113438839619367078813101134	25052230727639666685406250059829	25079114423870495712236143223769	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:44:23.902671+07
25079114423902039818602845947145	25059234741996733039402702628011	25052230727639666685406250059829	25079114423870495712236143223769	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:44:23.902671+07
25079114423918300691666861957590	25061111625457403196441300829178	25052230727639666685406250059829	25079114423870495712236143223769	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:44:23.918379+07
25079114819993458067162245746580	25059224212795097341037050503575	25052230727639666685406250059829	25079114819967009483348272929775	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:19.992409+07
25079114819996402617120961492661	25055133853118225902485283188890	25052230727639666685406250059829	25079114819967009483348272929775	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:19.996396+07
25079114819999827157846464258904	25055113438839619367078813101134	25052230727639666685406250059829	25079114819967009483348272929775	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:19.999911+07
25079114819999612442020067818213	25059234741996733039402702628011	25052230727639666685406250059829	25079114819967009483348272929775	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:19.999911+07
25079114820009749449509824771633	25061111625457403196441300829178	25052230727639666685406250059829	25079114819967009483348272929775	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:20.009228+07
25079114825842496819536640094151	25059224212795097341037050503575	25052230727639666685406250059829	25079114825812221068827124261192	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:25.840716+07
25079114825846479828584307293359	25055133853118225902485283188890	25052230727639666685406250059829	25079114825812221068827124261192	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:25.846498+07
25079114825850394578589545654727	25055113438839619367078813101134	25052230727639666685406250059829	25079114825812221068827124261192	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:25.850522+07
25079114825854962570663473761335	25059234741996733039402702628011	25052230727639666685406250059829	25079114825812221068827124261192	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:25.854543+07
25079114825858774051418931274557	25061111625457403196441300829178	25052230727639666685406250059829	25079114825812221068827124261192	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:25.858563+07
25079114836682144794771178415017	25059224212795097341037050503575	25052230727639666685406250059829	25079114836660715803685250046124	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:36.682685+07
25079114836688936100086215732101	25055133853118225902485283188890	25052230727639666685406250059829	25079114836660715803685250046124	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:36.688652+07
25079114836694965179414434495829	25055113438839619367078813101134	25052230727639666685406250059829	25079114836660715803685250046124	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:36.694383+07
25079114836700459903489288894879	25059234741996733039402702628011	25052230727639666685406250059829	25079114836660715803685250046124	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:36.700257+07
25079114836701911023955474037641	25061111625457403196441300829178	25052230727639666685406250059829	25079114836660715803685250046124	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 11:48:36.701174+07
25079130559255557372054235362229	25059224212795097341037050503575	25052230727639666685406250059829	25079130559226713413538060685176	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:05:59.253167+07
25079130559260244088951697610855	25055133853118225902485283188890	25052230727639666685406250059829	25079130559226713413538060685176	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:05:59.260242+07
25079130559265087940603916229251	25055113438839619367078813101134	25052230727639666685406250059829	25079130559226713413538060685176	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:05:59.264712+07
25079130559268576228509551877133	25059234741996733039402702628011	25052230727639666685406250059829	25079130559226713413538060685176	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:05:59.268305+07
25079130559269006447777801587619	25061111625457403196441300829178	25052230727639666685406250059829	25079130559226713413538060685176	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:05:59.269886+07
25079131520555841588293670521087	25059224212795097341037050503575	25052230727639666685406250059829	25079131520535388083159459216304	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:20.555185+07
25079131520562311194631315196386	25055133853118225902485283188890	25052230727639666685406250059829	25079131520535388083159459216304	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:20.562345+07
25079131520568807518735304670635	25055113438839619367078813101134	25052230727639666685406250059829	25079131520535388083159459216304	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:20.567726+07
25079131520569255971277035250622	25059234741996733039402702628011	25052230727639666685406250059829	25079131520535388083159459216304	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:20.56932+07
25079131520576236361214975390391	25061111625457403196441300829178	25052230727639666685406250059829	25079131520535388083159459216304	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:20.576091+07
25079131531567589917587951211591	25059224212795097341037050503575	25052230727639666685406250059829	25079131531549030492221301646288	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:31.567733+07
25079131531567541720367009343842	25055133853118225902485283188890	25052230727639666685406250059829	25079131531549030492221301646288	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:31.567733+07
25079131531579953361610591182018	25055113438839619367078813101134	25052230727639666685406250059829	25079131531549030492221301646288	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:31.579065+07
25079131531579620255409315287785	25059234741996733039402702628011	25052230727639666685406250059829	25079131531549030492221301646288	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:31.579065+07
25079131531579668597313487596332	25061111625457403196441300829178	25052230727639666685406250059829	25079131531549030492221301646288	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:15:31.579065+07
25079131620666254764578044259811	25059224212795097341037050503575	25052230727639666685406250059829	25079131620656009102540198267224	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:20.666463+07
25079131620666938258345038704816	25055133853118225902485283188890	25052230727639666685406250059829	25079131620656009102540198267224	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:20.666463+07
25079131620680030527423578542407	25055113438839619367078813101134	25052230727639666685406250059829	25079131620656009102540198267224	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:20.680567+07
25079131620682689525390577656271	25059234741996733039402702628011	25052230727639666685406250059829	25079131620656009102540198267224	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:20.682594+07
25079131620682040269126449401850	25061111625457403196441300829178	25052230727639666685406250059829	25079131620656009102540198267224	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:20.682594+07
25079131641972573542972688127054	25059224212795097341037050503575	25052230727639666685406250059829	25079131641957956011994810695868	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:41.972866+07
25079131641972550931344293028204	25055133853118225902485283188890	25052230727639666685406250059829	25079131641957956011994810695868	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:41.972866+07
25079131641989443616291350900464	25055113438839619367078813101134	25052230727639666685406250059829	25079131641957956011994810695868	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:41.989043+07
25079131641991322303354725097698	25059234741996733039402702628011	25052230727639666685406250059829	25079131641957956011994810695868	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:41.991978+07
25079131641996028737400296009775	25061111625457403196441300829178	25052230727639666685406250059829	25079131641957956011994810695868	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:16:41.996016+07
25079131842947682937018077160542	25059224212795097341037050503575	25052230727639666685406250059829	25079131842931881388097239693199	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:18:42.947655+07
25079131842950378950448676635181	25055133853118225902485283188890	25052230727639666685406250059829	25079131842931881388097239693199	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:18:42.950434+07
25079131842950344470814582515866	25055113438839619367078813101134	25052230727639666685406250059829	25079131842931881388097239693199	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:18:42.950434+07
25079131842950852919911936259611	25059234741996733039402702628011	25052230727639666685406250059829	25079131842931881388097239693199	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:18:42.950434+07
25079131842963728673265580230670	25061111625457403196441300829178	25052230727639666685406250059829	25079131842931881388097239693199	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:18:42.963467+07
25079131930244936065918526762829	25059224212795097341037050503575	25052230727639666685406250059829	25079131930236287664594925357090	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:19:30.244433+07
25079131930244156101168976762385	25055133853118225902485283188890	25052230727639666685406250059829	25079131930236287664594925357090	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:19:30.244433+07
25079131930244171420583264619683	25055113438839619367078813101134	25052230727639666685406250059829	25079131930236287664594925357090	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:19:30.244433+07
25079131930244771423535050023276	25059234741996733039402702628011	25052230727639666685406250059829	25079131930236287664594925357090	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:19:30.244433+07
25079131930252850878097241357938	25061111625457403196441300829178	25052230727639666685406250059829	25079131930236287664594925357090	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:19:30.252417+07
25079132203316703871316835400039	25059224212795097341037050503575	25052230727639666685406250059829	25079132203305069638540728941003	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:03.316234+07
25079132203318633038350321139069	25055133853118225902485283188890	25052230727639666685406250059829	25079132203305069638540728941003	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:03.318349+07
25079132203319036401707286691300	25055113438839619367078813101134	25052230727639666685406250059829	25079132203305069638540728941003	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:03.319942+07
25079132203321936503953260564373	25059234741996733039402702628011	25052230727639666685406250059829	25079132203305069638540728941003	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:03.321561+07
25079132203322560694071326442790	25061111625457403196441300829178	25052230727639666685406250059829	25079132203305069638540728941003	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:03.322658+07
25079132255214185204007884389765	25059224212795097341037050503575	25052230727639666685406250059829	25079132255203266795331133189530	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:55.214443+07
25079132255214511785055367159608	25055133853118225902485283188890	25052230727639666685406250059829	25079132255203266795331133189530	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:55.214443+07
25079132255214370777569730825785	25055113438839619367078813101134	25052230727639666685406250059829	25079132255203266795331133189530	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:55.214443+07
25079132255214922236016028423825	25059234741996733039402702628011	25052230727639666685406250059829	25079132255203266795331133189530	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:55.214443+07
25079132255214345074306166849345	25061111625457403196441300829178	25052230727639666685406250059829	25079132255203266795331133189530	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:22:55.214443+07
25079132353256652885010926796622	25059224212795097341037050503575	25052230727639666685406250059829	25079132353240869190578522674068	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:23:53.256115+07
25079132353256277539747330422406	25055133853118225902485283188890	25052230727639666685406250059829	25079132353240869190578522674068	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:23:53.256115+07
25079132353264429916449019888089	25055113438839619367078813101134	25052230727639666685406250059829	25079132353240869190578522674068	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:23:53.264684+07
25079132353272417935381592808899	25059234741996733039402702628011	25052230727639666685406250059829	25079132353240869190578522674068	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:23:53.27211+07
25079132353274595818527388304228	25061111625457403196441300829178	25052230727639666685406250059829	25079132353240869190578522674068	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:23:53.274135+07
25079132421316416179245771973161	25059224212795097341037050503575	25052230727639666685406250059829	25079132421295337828117627754767	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:21.316416+07
25079132421316211739122327244740	25055133853118225902485283188890	25052230727639666685406250059829	25079132421295337828117627754767	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:21.316416+07
25079132421324533833988727770534	25055113438839619367078813101134	25052230727639666685406250059829	25079132421295337828117627754767	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:21.316416+07
25079132421324547897388571446058	25059234741996733039402702628011	25052230727639666685406250059829	25079132421295337828117627754767	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:21.324866+07
25079132421324607687023739655208	25061111625457403196441300829178	25052230727639666685406250059829	25079132421295337828117627754767	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:21.324866+07
25079132450950137267005047328954	25059224212795097341037050503575	25052230727639666685406250059829	25079132450933051191447083402792	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:50.950005+07
25079132450957819324616045837566	25055133853118225902485283188890	25052230727639666685406250059829	25079132450933051191447083402792	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:50.957262+07
25079132450957343317256207883224	25055113438839619367078813101134	25052230727639666685406250059829	25079132450933051191447083402792	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:50.957262+07
25079132450957345772264243595297	25059234741996733039402702628011	25052230727639666685406250059829	25079132450933051191447083402792	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:50.957262+07
25079132450957565848653495325894	25061111625457403196441300829178	25052230727639666685406250059829	25079132450933051191447083402792	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:24:50.957262+07
25079133205617935436073657360689	25059224212795097341037050503575	25052230727639666685406250059829	25079133205611520775929308042006	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:05.617006+07
25079133205619446461678274502489	25055133853118225902485283188890	25052230727639666685406250059829	25079133205611520775929308042006	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:05.619018+07
25079133205621027951904300675268	25055113438839619367078813101134	25052230727639666685406250059829	25079133205611520775929308042006	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:05.621005+07
25079133205623565530850629573034	25059234741996733039402702628011	25052230727639666685406250059829	25079133205611520775929308042006	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:05.623283+07
25079133205625094601610392242240	25061111625457403196441300829178	25052230727639666685406250059829	25079133205611520775929308042006	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:05.624285+07
25079133242631966559176710917594	25059224212795097341037050503575	25052230727639666685406250059829	25079133242623000853870461040964	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:42.631873+07
25079133242635917924173127486697	25055133853118225902485283188890	25052230727639666685406250059829	25079133242623000853870461040964	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:42.635121+07
25079133242638999468696769616569	25055113438839619367078813101134	25052230727639666685406250059829	25079133242623000853870461040964	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:42.63865+07
25079133242640642091223250492557	25059234741996733039402702628011	25052230727639666685406250059829	25079133242623000853870461040964	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:42.640154+07
25079133242641278849657894223189	25061111625457403196441300829178	25052230727639666685406250059829	25079133242623000853870461040964	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:32:42.640154+07
25079133514822500936357333349933	25059224212795097341037050503575	25052230727639666685406250059829	25079133514815985795417210203332	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:35:14.821486+07
25079133514826435092264926947454	25055133853118225902485283188890	25052230727639666685406250059829	25079133514815985795417210203332	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:35:14.826505+07
25079133514830145745994089056603	25055113438839619367078813101134	25052230727639666685406250059829	25079133514815985795417210203332	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:35:14.829048+07
25079133514832676490260278394583	25059234741996733039402702628011	25052230727639666685406250059829	25079133514815985795417210203332	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:35:14.832578+07
25079133514834834570143666579802	25061111625457403196441300829178	25052230727639666685406250059829	25079133514815985795417210203332	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:35:14.834573+07
25079133631338515269578926212962	25059224212795097341037050503575	25052230727639666685406250059829	25079133631328884136672735402832	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:36:31.338733+07
25079133631339018014934494259729	25055133853118225902485283188890	25052230727639666685406250059829	25079133631328884136672735402832	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:36:31.339769+07
25079133631341900912997021810788	25055113438839619367078813101134	25052230727639666685406250059829	25079133631328884136672735402832	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:36:31.339769+07
25079133631341618121151274021687	25059234741996733039402702628011	25052230727639666685406250059829	25079133631328884136672735402832	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:36:31.341277+07
25079133631343065854430992248898	25061111625457403196441300829178	25052230727639666685406250059829	25079133631328884136672735402832	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:36:31.343288+07
25079133857156001448943605099449	25059224212795097341037050503575	25052230727639666685406250059829	25079133857143117382513249690472	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:38:57.156142+07
25079133857158925618943340815972	25055133853118225902485283188890	25052230727639666685406250059829	25079133857143117382513249690472	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:38:57.158139+07
25079133857159262716234705505267	25055113438839619367078813101134	25052230727639666685406250059829	25079133857143117382513249690472	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:38:57.15914+07
25079133857159583029449200560776	25059234741996733039402702628011	25052230727639666685406250059829	25079133857143117382513249690472	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:38:57.15914+07
25079133857160063959887123150305	25061111625457403196441300829178	25052230727639666685406250059829	25079133857143117382513249690472	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:38:57.160645+07
25079134000328364444631570796844	25059224212795097341037050503575	25052230727639666685406250059829	25079134000317742863252861824784	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:00.328737+07
25079134000330989488160387834276	25055133853118225902485283188890	25052230727639666685406250059829	25079134000317742863252861824784	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:00.330245+07
25079134000331318211211069150320	25055113438839619367078813101134	25052230727639666685406250059829	25079134000317742863252861824784	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:00.33125+07
25079134000332571842653333759811	25059234741996733039402702628011	25052230727639666685406250059829	25079134000317742863252861824784	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:00.332648+07
25079134000333137237875933076926	25061111625457403196441300829178	25052230727639666685406250059829	25079134000317742863252861824784	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:00.333655+07
25079134038260991509651915793179	25059224212795097341037050503575	25052230727639666685406250059829	25079134038249339227935604596479	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:38.260243+07
25079134038260833103596438261908	25055133853118225902485283188890	25052230727639666685406250059829	25079134038249339227935604596479	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:38.260845+07
25079134038262143047906307877104	25055113438839619367078813101134	25052230727639666685406250059829	25079134038249339227935604596479	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:38.262198+07
25079134038263207368723648201402	25059234741996733039402702628011	25052230727639666685406250059829	25079134038249339227935604596479	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:38.263205+07
25079134038264609615709888636883	25061111625457403196441300829178	25052230727639666685406250059829	25079134038249339227935604596479	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:40:38.264205+07
25079134141065750300224026424373	25059224212795097341037050503575	25052230727639666685406250059829	25079134141054911434020088331005	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:41:41.065254+07
25079134141066821908190353411790	25055133853118225902485283188890	25052230727639666685406250059829	25079134141054911434020088331005	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:41:41.066291+07
25079134141067706590831292364689	25055113438839619367078813101134	25052230727639666685406250059829	25079134141054911434020088331005	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:41:41.067428+07
25079134141068847814191360212396	25059234741996733039402702628011	25052230727639666685406250059829	25079134141054911434020088331005	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:41:41.06829+07
25079134141070207409997776417268	25061111625457403196441300829178	25052230727639666685406250059829	25079134141054911434020088331005	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:41:41.070266+07
25079134214737235614371826464475	25059224212795097341037050503575	25052230727639666685406250059829	25079134214726073860092970288382	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:14.737304+07
25079134214738886911133370284910	25055133853118225902485283188890	25052230727639666685406250059829	25079134214726073860092970288382	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:14.738165+07
25079134214739050480392447112163	25055113438839619367078813101134	25052230727639666685406250059829	25079134214726073860092970288382	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:14.739196+07
25079134214740647037705645135402	25059234741996733039402702628011	25052230727639666685406250059829	25079134214726073860092970288382	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:14.740202+07
25079134214741018764409632051968	25061111625457403196441300829178	25052230727639666685406250059829	25079134214726073860092970288382	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:14.741208+07
25079134257745661573704584304403	25059224212795097341037050503575	25052230727639666685406250059829	25079134257735705948308758948072	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:57.745538+07
25079134257747980515965458888713	25055133853118225902485283188890	25052230727639666685406250059829	25079134257735705948308758948072	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:57.747558+07
25079134257748861724429786848817	25055113438839619367078813101134	25052230727639666685406250059829	25079134257735705948308758948072	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:57.748538+07
25079134257748983125937690665082	25059234741996733039402702628011	25052230727639666685406250059829	25079134257735705948308758948072	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:57.748538+07
25079134257750310900819655859785	25061111625457403196441300829178	25052230727639666685406250059829	25079134257735705948308758948072	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:42:57.750079+07
25079134441107117844610522280910	25059224212795097341037050503575	25052230727639666685406250059829	25079134441098543139482513376465	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:44:41.107783+07
25079134441109362309035273182549	25055133853118225902485283188890	25052230727639666685406250059829	25079134441098543139482513376465	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:44:41.109291+07
25079134441110121791530889811931	25055113438839619367078813101134	25052230727639666685406250059829	25079134441098543139482513376465	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:44:41.110301+07
25079134441111030380852159448813	25059234741996733039402702628011	25052230727639666685406250059829	25079134441098543139482513376465	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:44:41.111299+07
25079134441112765857337033731135	25061111625457403196441300829178	25052230727639666685406250059829	25079134441098543139482513376465	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:44:41.112665+07
25079134600508339192231095422859	25059224212795097341037050503575	25052230727639666685406250059829	25079134600504040667220634485389	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:46:00.5071+07
25079134600509747077596002992948	25055133853118225902485283188890	25052230727639666685406250059829	25079134600504040667220634485389	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:46:00.509202+07
25079134600509518470471759726307	25055113438839619367078813101134	25052230727639666685406250059829	25079134600504040667220634485389	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:46:00.509202+07
25079134600510646991352902004888	25059234741996733039402702628011	25052230727639666685406250059829	25079134600504040667220634485389	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:46:00.510875+07
25079134600512070253806639675337	25061111625457403196441300829178	25052230727639666685406250059829	25079134600504040667220634485389	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:46:00.51242+07
25079134733529512167611195274075	25059224212795097341037050503575	25052230727639666685406250059829	25079134733519413682186048269691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:47:33.529619+07
25079134733531881452342477376239	25055133853118225902485283188890	25052230727639666685406250059829	25079134733519413682186048269691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:47:33.53154+07
25079134733532178257434547580581	25055113438839619367078813101134	25052230727639666685406250059829	25079134733519413682186048269691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:47:33.532624+07
25079134733533617297601034938082	25059234741996733039402702628011	25052230727639666685406250059829	25079134733519413682186048269691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:47:33.533631+07
25079134733534601823263830117143	25061111625457403196441300829178	25052230727639666685406250059829	25079134733519413682186048269691	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-20 13:47:33.534631+07
25080100937436522980517206363153	25055113438839619367078813101134	25052230727639666685406250059829	\N	FRIEND_REQUEST	Bạn nhận được lời mời kết bạn.	\N	f	2025-03-21 10:09:37.428319+07
25080101027749215834841428157482	25055113902427548222505933023551	25055113438839619367078813101134	25080101027728392982461345812123	NEW_STORIES	Dang Xuan Hieu vừa đăng tin mới	\N	f	2025-03-21 10:10:27.74211+07
25080134556577575283569132802198	25055113438839619367078813101134	25052230727639666685406250059829	\N	FRIEND_REQUEST	Bạn nhận được lời mời kết bạn.	\N	f	2025-03-21 13:45:56.573319+07
25080135918834431921466324477050	25052230727639666685406250059829	25055113438839619367078813101134	\N	FRIEND_REQUEST	Bạn nhận được lời mời kết bạn.	\N	f	2025-03-21 13:59:18.830049+07
25080140102925840124890601457045	25052230727639666685406250059829	25055113438839619367078813101134	\N	FRIEND_REQUEST	Bạn nhận được lời mời kết bạn.	\N	f	2025-03-21 14:01:02.923469+07
\.


--
-- TOC entry 3457 (class 0 OID 17415)
-- Dependencies: 224
-- Data for Name: page_followers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.page_followers (id, page_id, user_id, created_at, page_role, status) FROM stdin;
25061004508534682492382550216120	25061004508465065265754961945281	25052230727639666685406250059829	2025-03-02 00:45:08.532898	ADMIN	1
25061004652499789381823175797824	25061004652496886971369924452985	25052230727639666685406250059829	2025-03-02 00:46:52.499608	ADMIN	1
25061005429124177296437421297488	25061005429102675554166703534952	25052230727639666685406250059829	2025-03-02 00:54:29.123153	ADMIN	1
25061005455465006458461108136803	25061005455460632441534177073945	25052230727639666685406250059829	2025-03-02 00:54:55.462642	ADMIN	1
25061123747243750784497279588811	25061123747135306040698232717910	25061111625457403196441300829178	2025-03-02 12:37:47.243926	ADMIN	1
25061140445296523873388172225702	25061004508465065265754961945281	25061111625457403196441300829178	2025-03-02 14:04:45.288818	MEMBER	1
25061140449825650234614713532998	25061004652496886971369924452985	25061111625457403196441300829178	2025-03-02 14:04:49.825457	MEMBER	1
\.


--
-- TOC entry 3456 (class 0 OID 17406)
-- Dependencies: 223
-- Data for Name: pages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pages (id, name, description, profile_image, cover_image, category, created_by, created_at, updated_at, deleted, regime) FROM stdin;
25061004508465065265754961945281	NUTRI DIAMON	bbbbbbbbbbbbbbbbbbbbbbbbbbbbb	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851108/vwiwvbqu1nezvfj7zrmk.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851106/fqcxtgp83vwwcup1vrew.jpg	1	25052230727639666685406250059829	2025-03-02 00:45:02.94244	2025-03-02 00:45:02.943439	f	0
25061004652496886971369924452985	NUTRI GOLD	ccccccccccccccccccccccccccccccc	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851212/dbstsleqwjtp14mgguq6.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851210/hmcitmjadisqppycdyxu.jpg	1	25052230727639666685406250059829	2025-03-02 00:46:48.785714	2025-03-02 00:46:48.785714	f	0
25061005429102675554166703534952	NUTRI FAMILY	ddddddddddddddddddddddddddddd	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851669/efr0dolqaxfhik4tsvcg.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851667/mcgzrvffkq5zbmpdmzvb.jpg	2	25052230727639666685406250059829	2025-03-02 00:54:23.94391	2025-03-02 00:54:23.94391	f	0
25061005455460632441534177073945	NUTRI FAMILY 2	ddddddddddddddddddddddddddddd	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851695/agegjimszp5lhdudca3g.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740851693/bq5nekefvsri6n0ycqyu.jpg	2	25052230727639666685406250059829	2025-03-02 00:54:51.504236	2025-03-02 00:54:51.504236	f	0
25061123747135306040698232717910	Theanh 28 Entertainment 	Tin tức, giải trí, hài hước	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740893867/edw3e4iy9khq5etcpc7n.jpg	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740893865/ffaksvpdahapd4jler54.jpg	2	25061111625457403196441300829178	2025-03-02 12:37:41.215822	2025-03-02 12:37:41.215822	f	0
\.


--
-- TOC entry 3450 (class 0 OID 17344)
-- Dependencies: 217
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (post_id, user_id, content, image_url, status, deleted, created_at, updated_at, tag, hashtag, likes, comments, type, share_info, privacy, type_file) FROM stdin;
25062214006175165208052432391804	25052230727639666685406250059829	Sẽ tuyệt vời hơn nếu có bạn ở bên 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012806/w0wi84zur4smesel7l0t.jpg"]	2	f	2025-03-03 21:40:01.678954+07	2025-03-03 21:40:01.678954+07	["[\\"25061111625457403196441300829178\\"", "\\"25059224212795097341037050503575\\"]"]	["[\\"TÌNH CẢM\\"]"]	0	0	NORMAL	\N	2	IMAGE
25062212942035500775219464998492	25052230727639666685406250059829	Thế giới này thật tươi đẹp biết bao 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012178/z853mzk1vcsru33sg8pt.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012180/wfjdgocf2obwsgzesfck.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012182/aqvxozodoiowqnjfkqtp.jpg"]	2	f	2025-03-03 21:29:33.895753+07	2025-03-03 21:29:33.895753+07	["[\\"25055113438839619367078813101134\\"]"]	["[\\"GIẢI TRÍ\\"]"]	0	0	NORMAL	\N	2	IMAGE
25064163743029502003629178646458	25055113902427548222505933023551	I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741167462/vdx3mypw42snv0wiioo7.jpg"]	0	f	2025-03-05 16:37:39.352418+07	2025-03-05 16:37:39.352418+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064154056510023066872340649333	25052230727639666685406250059829	Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164054/uohsoh59ec0pnkwhu3rp.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164056/u48fxg37if8jl71yibcx.jpg"]	0	f	2025-03-05 15:40:51.455755+07	2025-03-05 15:40:51.455755+07	["[]"]	["[]"]	0	0	NORMAL	\N	1	IMAGE
25064155227053274620517829051811	25052230727639666685406250059829	Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164746/lhmckhgvrgxdntnyfufr.jpg"]	0	f	2025-03-05 15:52:24.856042+07	2025-03-05 15:52:24.856042+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064155401842397344689689943249	25059234741996733039402702628011	Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164841/n6shw180whp44pwc8l9y.jpg"]	0	f	2025-03-05 15:54:00.260377+07	2025-03-05 15:54:00.260377+07	["[]"]	["[]"]	0	0	NORMAL	\N	1	IMAGE
25064155447175401642305240130484	25059234741996733039402702628011	Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old.	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164887/tzszhnfuitngp4mxyqkp.jpg"]	0	f	2025-03-05 15:54:45.401665+07	2025-03-05 15:54:45.401665+07	["[]"]	["[]"]	0	0	NORMAL	\N	1	IMAGE
25064151943026776055926400843284	25052230727639666685406250059829	Get the Bundesliga Pass on the OneFootball App	["http://res.cloudinary.com/dqsl5u4zc/video/upload/v1741162782/vjmqffdqacnialimas4o.mp4"]	0	f	2025-03-05 15:19:38.094599+07	2025-03-05 15:19:38.094599+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	VIDEO
25063084032812914737774889984140	25052230727639666685406250059829	Wao wao wao tuyệt quá đi 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741052432/lknkhq4w3pnzjirqrmtq.jpg"]	0	f	2025-03-04 08:40:30.030785+07	2025-03-04 08:40:30.030785+07	["[\\"25055113438839619367078813101134\\"]"]	["[\\"HÀI HƯỚC\\"]"]	0	0	NORMAL	\N	2	IMAGE
25064155534972259165521359816437	25059234741996733039402702628011	Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164934/pq9stjz2qpcvklkzi2xj.jpg"]	0	f	2025-03-05 15:55:33.337556+07	2025-03-05 15:55:33.337556+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25063115521579829175337267050499	25052230727639666685406250059829	Maybach 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741064121/f73y13aiafxehwhgij9y.png"]	0	f	2025-03-04 11:55:19.169292+07	2025-03-04 11:55:19.169292+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25062214232374622591416382064677	25055113438839619367078813101134	Cảm ơn bạn vì đã đến	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012951/ebsibv1qpxk3gukigefj.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741012952/bf1faw9u0m9nji7qg6th.png"]	2	f	2025-03-03 21:42:27.933238+07	2025-03-03 21:42:27.933238+07	["[\\"25052230727639666685406250059829\\"]"]	["[\\"TÌNH CẢM\\"]"]	0	0	NORMAL	\N	2	IMAGE
25063105639832058533475686133423	25052230727639666685406250059829	Đối thủ duy nhất của tôi chính là tôi ngày hôm qua	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741060592/x9vcxyoqidmhqbmvlojn.png", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741060594/gw34edli6tcs9jgdxj5j.png", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741060597/j9fmc5yfawxmfutergpw.png", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741060599/o4kkqbkej3u9fycnr62s.png"]	0	f	2025-03-04 10:56:31.235441+07	2025-03-04 10:56:31.235441+07	["[\\"25055133853118225902485283188890\\"]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25065092232325717763311984309921	25052230727639666685406250059829	aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741227752/x9qxg3sfsyk6qbzyeybe.jpg"]	0	f	2025-03-06 09:22:29.274693+07	2025-03-06 09:22:29.274693+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064155631020279488456521914612	25059234822041495869043745170436	There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741164990/dmwerdxsxnrfpdcpuumg.jpg"]	0	f	2025-03-05 15:56:29.227638+07	2025-03-05 15:56:29.227638+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064155741658932709511615831625	25059234822041495869043745170436	All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet.	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741165057/gs1m6zqyfiqouy7wcyg1.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741165059/oqen9yhmqdkypdolexnq.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741165061/mit0vpbi6m6mutakgfcx.jpg"]	0	f	2025-03-05 15:57:34.139156+07	2025-03-05 15:57:34.139156+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064155921110097048815740293572	25059224212795097341037050503575	The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741165161/wahygpzjfemyjlk36wuh.jpg"]	0	f	2025-03-05 15:59:19.213888+07	2025-03-05 15:59:19.213888+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25064163612416365518707227292373	25059234229788357447930868028765	Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1741167370/vevrmif8zueiszzeghhf.jpg"]	0	f	2025-03-05 16:36:07.634062+07	2025-03-05 16:36:07.634062+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25065092421798831232822204537393	25052230727639666685406250059829	Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centurie	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741227861/sy5m71ybpqjd8mygposp.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741227861/zzskrervw7ofuqu6wjsq.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741227861/ghgnvtxfh77p2cmuerey.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741227861/kaogmragkert4z1veha8.jpg"]	0	f	2025-03-06 09:24:19.208474+07	2025-03-06 09:24:19.208474+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25065092805367927358814753318898	25052230727639666685406250059829		["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741228085/ry4whggouqrj39xffeoj.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741228085/vljjuouldvdgdntyz8fv.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741228084/qcs6g9dxg0bydsfhu8lk.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741228085/wshsnbw2xmpscllwc4w3.jpg"]	0	f	2025-03-06 09:28:03.091911+07	2025-03-06 09:28:03.091911+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25066085521877389994042652563968	25052230727639666685406250059829	Cách này sẽ đảm bảo 3 ảnh bên phải (trong trường hợp 4 ảnh) được phân bố đều theo chiều cao, mỗi ảnh chiếm 1/3 chiều cao của container, đảm bảo không có ảnh nào bị ẩn.	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741312521/cncgpdkyzzmoaskxzxbo.jpg"]	0	f	2025-03-07 08:55:18.660561+07	2025-03-07 08:55:18.660561+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25066090033292478411512775011350	25052230727639666685406250059829	Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741312833/rtl5qgx5u1xagoqpghu3.jpg"]	0	f	2025-03-07 09:00:30.068172+07	2025-03-07 09:00:30.068172+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
25066091059535792670709816043812	25052230727639666685406250059829	This expedient serves to get an idea of the finished product that will soon be printed or disseminated via digital channels.\r\n\r\n	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313383/sidayi64zzjoq8vzbcxt.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313383/nvvrswquhf0ppers1xfr.jpg"]	0	f	2025-03-07 09:09:39.030587+07	2025-03-11 08:30:30.63511+07	["[]"]	["[]"]	1	0	NORMAL	\N	2	IMAGE
25073085212739517750691635263012	25055113438839619367078813101134	lambogini quá đẹp 	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741917133/l9x9my9ijmdijroblpcn.jpg"]	0	f	2025-03-14 08:52:09.619762+07	2025-03-14 08:52:09.619762+07	["[]"]	["[]"]	0	0	NORMAL	\N	1	IMAGE
25073085508560944501417325209663	25055113438839619367078813101134	Ferari ddejp ghe	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741917309/gfaqxlz8cegyfqs394hx.jpg"]	0	f	2025-03-14 08:55:06.052392+07	2025-03-14 08:55:06.052392+07	["[]"]	["[]"]	0	0	NORMAL	\N	1	IMAGE
25066091304658516345385365729210	25052230727639666685406250059829	Lorem ipsum is a nonsensical Latin text used to fill empty spaces in graphic design, publishing, and web development.	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313584/bique7elfgdpxf4wzsrk.jpg"]	0	f	2025-03-07 09:13:01.489304+07	2025-03-14 09:12:08.407625+07	["[]"]	["[]"]	1	0	NORMAL	\N	2	IMAGE
25066090742252820313101519604343	25052230727639666685406250059829	This expedient serves to get an idea of the finished product that will soon be printed or disseminated via digital channels.\r\n\r\n	["https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313260/eoaapzgfdebzi298fra3.jpg"]	0	f	2025-03-07 09:07:38.087083+07	2025-03-10 08:28:13.496766+07	["[]"]	["[]"]	0	0	NORMAL	\N	2	IMAGE
\.


--
-- TOC entry 3459 (class 0 OID 17442)
-- Dependencies: 226
-- Data for Name: stories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stories (id, user_id, content, video_url, background_color, is_active, type, created_at, updated_at, expires_at) FROM stdin;
25079134038249339227935604596479	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742452837/nnieepbpkonpvsg3ylil.png	#000000	t	IMAGE	2025-03-20 13:40:36.021281	2025-03-20 13:40:36.021281	2025-03-22 06:40:35.847
25079134141054911434020088331005	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742452900/hodnghy9vqtkpqzfwzfj.png	#000000	t	IMAGE	2025-03-20 13:41:38.57452	2025-03-20 13:41:38.57452	2025-03-22 06:41:38.4
25079132255203266795331133189530	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742451774/bwdb96fzt3ft0f5d7ra9.png	#000000	t	IMAGE	2025-03-20 13:22:52.310276	2025-03-20 13:22:52.310276	2025-03-22 06:22:52.138
25079132353240869190578522674068	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742451832/ovri1s8laeh0a0h4plk6.png	#000000	t	IMAGE	2025-03-20 13:23:50.669383	2025-03-20 13:23:50.669383	2025-03-22 06:23:50.435
25079132421295337828117627754767	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742451860/qcdsmbaz9pbcns3dxosx.png	#000000	t	IMAGE	2025-03-20 13:24:19.051943	2025-03-20 13:24:19.051943	2025-03-22 06:24:18.773
25079134441098543139482513376465	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742453080/xbhzt6aoekuazq7freor.png	#000000	t	IMAGE	2025-03-20 13:44:38.537376	2025-03-20 13:44:38.537376	2025-03-22 06:44:38.364
25079134600504040667220634485389	25052230727639666685406250059829		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742453160/xznqmlbibluld4kbonjo.png	#000000	t	IMAGE	2025-03-20 13:45:58.096976	2025-03-20 13:45:58.096976	2025-03-22 06:45:57.931
25080101027728392982461345812123	25055113438839619367078813101134		http://res.cloudinary.com/dqsl5u4zc/image/upload/v1742526625/prr5jvaufsk5vfafzkem.png	#000000	t	IMAGE	2025-03-21 10:10:24.158797	2025-03-21 10:10:24.158797	2025-03-23 03:10:23.63
\.


--
-- TOC entry 3449 (class 0 OID 17331)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, username, email, password, age, full_name, avatar_url, education_level, gender, status, address, phone_number, description, created_at, updated_at, access_token_reset_at, failed_login_count, date_of_birth, list_images, count_likes, count_follows) FROM stdin;
25059224212795097341037050503575	ncquoc	nqh0032@gmail.com	ccd8f4f47d84a6d7b6b5344b61d3970196093e7d14687285b3c39f652848714f	\N	Nguyen Chi  Quoc	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0111223345	\N	2025-02-28 22:42:12.78076	2025-02-28 22:42:12.78076	2025-02-28 22:42:12.836894	\N	2025-02-11	\N	\N	\N
25055133853118225902485283188890	huynt	huynt1102@gmail.com	e608dcb6866dac256d0230eb3b9b438b268b8187ad20988b583c6540259be1a8	14	QDH	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379131/jvpbaeaoi6hwkdg2dbin.jpg	DHCNHN	1	1	Xuan Dai, Xuan Truong, Nam Dinh	082341255	AAAAAAAAAAAAA met qua di	2025-02-24 13:38:50.739083	2025-02-24 13:38:50.739083	2025-02-24 13:38:53.206945	0	2025-02-18	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379131/jvpbaeaoi6hwkdg2dbin.jpg"]	0	0
25055134706490805496240886736318	haonq	haonq220102@gmail.com	31a2dcbae6e6793784b0bee7015ba61e45a4ca62a5a1f16896db21ffa0ef6a48	22	NQH	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379625/hduyj0psqb2nqhysps5m.jpg	DHCNHN	1	1	Xuan Dai, Xuan Truong, Nam Dinh	082341257	AAAAAAAAAAAAA met qua di	2025-02-24 13:47:03.987897	2025-02-24 13:47:03.987897	2025-02-24 13:47:06.551872	1	2002-11-13	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379625/hduyj0psqb2nqhysps5m.jpg"]	0	0
25059224522572828172277451169875	ncphi	nqh003231232@gmail.com	0b23d340b4c72abbfd9ef48162a065550d33b9b64dae38ac97303ebe0d4e7197	\N	Nguyen Cao Phi	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	01112243442	\N	2025-02-28 22:45:22.569283	2025-02-28 22:45:22.569283	2025-02-28 22:45:22.58222	\N	2025-02-20	\N	\N	\N
25059234123604113011211455616547	pqdang	xuanhieu00121@gmail.com	47805599c5732160c49bc7ccabc7c6bfe525697dd8a7ad2d0fc6544f52f0644b	\N	Pham Quoc Dang	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0327182532	\N	2025-02-28 23:41:23.590262	2025-02-28 23:41:23.590262	2025-02-28 23:41:23.652012	1	2025-01-29	\N	\N	\N
25055113438839619367078813101134	xuanhieu2	xuanhieu1102@gmail.com	83f73e5b4f586f77668e3f8d53df46f3fa298643a6dd1740fc68bf703fdca170	23	Dang Xuan Hieu	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379369/qkxxm5dbr4fghu9pgmia.jpg	DHCNHN	1	1	Xuan Dai, Xuan Truong, Nam Dinh	082341251	AAAAAAAAAAAAA met qua di	2025-02-24 11:34:35.634889	2025-02-24 11:34:35.634889	2025-02-24 11:34:38.888728	1	2025-02-11	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740371676/mfcwh2ge4johu6zg8fjg.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379283/b2c95itygthjgmyktfzw.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379369/qkxxm5dbr4fghu9pgmia.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741917133/l9x9my9ijmdijroblpcn.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741917309/gfaqxlz8cegyfqs394hx.jpg"]	0	0
25052230727639666685406250059829	xuanhieu	xuanhieu0031@gmail.com	941474caf1db4d789deccc5d7e65f8117e18b880522c6d470b5739b51eb7ba06	22	Dang Xuan Hieu	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg	DHCNHN	1	1	Xuan Dai, Xuan Truong, Nam Dinh	0858250715	Yeu mau hong ghet su gia doi	2025-02-21 23:07:23.985938	2025-02-21 23:07:23.985938	2025-02-24 09:48:47.412747	1	2002-02-13	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740154048/ygbjc8mnk8bgwasittat.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740371676/mfcwh2ge4johu6zg8fjg.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379283/b2c95itygthjgmyktfzw.jpg", "http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740379369/qkxxm5dbr4fghu9pgmia.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741312521/cncgpdkyzzmoaskxzxbo.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741312833/rtl5qgx5u1xagoqpghu3.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313260/eoaapzgfdebzi298fra3.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313383/sidayi64zzjoq8vzbcxt.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313383/nvvrswquhf0ppers1xfr.jpg", "https://res.cloudinary.com/dqsl5u4zc/image/upload/v1741313584/bique7elfgdpxf4wzsrk.jpg"]	0	0
25059234229788357447930868028765	dhhanh	xuanhieu0211@gmail.com	b89ebffd1c0526174f3f6c11a6d2141446b60c2b42bf903220848bfc149ad6e5	\N	Dang   Hanh	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0767146114	\N	2025-02-28 23:42:29.788139	2025-02-28 23:42:29.788139	2025-02-28 23:42:29.797087	\N	2025-02-11	\N	\N	\N
25059223744436012323964224169985	nqhuy	nqh0031@gmail.com	54cf4571a4b2f90f50ee5f29abb02860453c4959430358c01f1b8a3389a7c32f	\N	Nguyen Quoc Huy	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0111223344	\N	2025-02-28 22:37:44.436116	2025-02-28 22:37:44.436116	2025-02-28 22:37:44.596173	\N	2025-02-11	["https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg"]	\N	\N
25059234351734700096766708535158	ptchinh	xuanhieu0036@gmail.com	76534f02362a162cc731ed80b19b385c04d689fc77046ae1ebb1f66b5c787df0	\N	Phan Tuan Chinh	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	3242427334	\N	2025-02-28 23:43:51.733591	2025-02-28 23:43:51.733591	2025-02-28 23:43:51.740587	\N	2025-01-29	\N	\N	\N
25059234822041495869043745170436	pqhuy	phiyieu003111111@gmail.com	eb1ef8b7c0500418d1f4239b484caf1c334cbcb782fe546bab8a3657ddf991de	\N	Phan Quoc Huy	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0858254124	\N	2025-02-28 23:48:22.040017	2025-02-28 23:48:22.040017	2025-02-28 23:48:22.045015	\N	2025-02-03	\N	\N	\N
25059234741996733039402702628011	pthong	pthong0031@gmail.com	4c94e9a773d120e954a3313ad301297d4e3931bd82ebf2c4e68a258a8e8364eb	\N	Phan Thi Hong	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0327182541	\N	2025-02-28 23:47:41.982598	2025-02-28 23:47:41.982598	2025-02-28 23:47:42.042043	\N	2025-01-29	\N	\N	\N
25059234845208633954427897551865	tthai	tthai0031@gmail.com	1ded3f486e76083f83c965f2e2777e3e5b1af119718f89965dcdab7eb0f8cec1	\N	Trinh Tuan Hai	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	0327182572	\N	2025-02-28 23:48:45.206171	2025-02-28 23:48:45.206171	2025-02-28 23:48:45.221166	\N	2025-02-13	\N	\N	\N
25061111625457403196441300829178	pnhuynh	nqhuynh119312@gmail.com	8d21c12e990503cb8858e6cdfbb2dd4c833c9166d91739ebd7ecc6091dfe1237	\N	Phan Ngoc Huynh	https://i.pinimg.com/736x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg	\N	2	2	\N	088866464	\N	2025-03-02 11:16:25.436037	2025-03-02 11:16:25.436037	2025-03-02 11:16:25.568332	\N	2025-03-19	\N	\N	\N
25055113902427548222505933023551	xuanhieu3	xuanhieu1103@gmail.com	9533e5c8e760ca044e77f689ca4bda7c78407c7f7b2f9e99d1d8bd83ecd8fc32	23	Dang Xuan Hieu	http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740371941/g4jlasoh7qgo7ufezjzt.jpg	DHCNHN	1	1	Xuan Dai, Xuan Truong, Nam Dinh	082341253	AAAAAAAAAAAAA met qua di	2025-02-24 11:39:00.55457	2025-02-24 11:39:00.55457	2025-02-24 11:39:02.432221	1	2025-02-04	["http://res.cloudinary.com/dqsl5u4zc/image/upload/v1740371941/g4jlasoh7qgo7ufezjzt.jpg"]	0	0
\.


--
-- TOC entry 3278 (class 2606 OID 17381)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3296 (class 2606 OID 17472)
-- Name: conversation_participants conversation_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversation_participants
    ADD CONSTRAINT conversation_participants_pkey PRIMARY KEY (id);


--
-- TOC entry 3292 (class 2606 OID 17462)
-- Name: conversations conversations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conversations
    ADD CONSTRAINT conversations_pkey PRIMARY KEY (id);


--
-- TOC entry 3261 (class 2606 OID 17330)
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- TOC entry 3269 (class 2606 OID 17359)
-- Name: friendships friendships_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT friendships_pkey PRIMARY KEY (id);


--
-- TOC entry 3282 (class 2606 OID 17405)
-- Name: group_members group_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.group_members
    ADD CONSTRAINT group_members_pkey PRIMARY KEY (id);


--
-- TOC entry 3280 (class 2606 OID 17395)
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- TOC entry 3276 (class 2606 OID 17371)
-- Name: likes likes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.likes
    ADD CONSTRAINT likes_pkey PRIMARY KEY (id);


--
-- TOC entry 3304 (class 2606 OID 17484)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- TOC entry 3288 (class 2606 OID 17441)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 3286 (class 2606 OID 17422)
-- Name: page_followers page_followers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.page_followers
    ADD CONSTRAINT page_followers_pkey PRIMARY KEY (id);


--
-- TOC entry 3284 (class 2606 OID 17414)
-- Name: pages pages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pages
    ADD CONSTRAINT pages_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 17451)
-- Name: stories stories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stories
    ADD CONSTRAINT stories_pkey PRIMARY KEY (id);


--
-- TOC entry 3263 (class 2606 OID 17343)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3265 (class 2606 OID 17339)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3267 (class 2606 OID 17341)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3297 (class 1259 OID 17473)
-- Name: idx_conversation_participants_conversation_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversation_participants_conversation_id ON public.conversation_participants USING btree (conversation_id);


--
-- TOC entry 3298 (class 1259 OID 17475)
-- Name: idx_conversation_participants_conversation_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversation_participants_conversation_user ON public.conversation_participants USING btree (conversation_id, user_id);


--
-- TOC entry 3299 (class 1259 OID 17474)
-- Name: idx_conversation_participants_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversation_participants_user_id ON public.conversation_participants USING btree (user_id);


--
-- TOC entry 3293 (class 1259 OID 17464)
-- Name: idx_conversations_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversations_created_at ON public.conversations USING btree (created_at);


--
-- TOC entry 3294 (class 1259 OID 17463)
-- Name: idx_conversations_type; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_conversations_type ON public.conversations USING btree (type);


--
-- TOC entry 3270 (class 1259 OID 17361)
-- Name: idx_friendships_friend_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friendships_friend_id ON public.friendships USING btree (friend_id);


--
-- TOC entry 3271 (class 1259 OID 17362)
-- Name: idx_friendships_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friendships_status ON public.friendships USING btree (status);


--
-- TOC entry 3272 (class 1259 OID 17363)
-- Name: idx_friendships_user_friend; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friendships_user_friend ON public.friendships USING btree (user_id, friend_id);


--
-- TOC entry 3273 (class 1259 OID 17360)
-- Name: idx_friendships_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_friendships_user_id ON public.friendships USING btree (user_id);


--
-- TOC entry 3274 (class 1259 OID 17372)
-- Name: idx_likes_post_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_likes_post_status ON public.likes USING btree (post_id, status);


--
-- TOC entry 3300 (class 1259 OID 17485)
-- Name: idx_messages_conversation_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_conversation_id ON public.messages USING btree (conversation_id);


--
-- TOC entry 3301 (class 1259 OID 17487)
-- Name: idx_messages_created_at; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_created_at ON public.messages USING btree (created_at);


--
-- TOC entry 3302 (class 1259 OID 17486)
-- Name: idx_messages_sender_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_messages_sender_id ON public.messages USING btree (sender_id);


-- Completed on 2025-03-23 10:45:58

--
-- PostgreSQL database dump complete
--

