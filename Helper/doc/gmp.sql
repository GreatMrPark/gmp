-- --------------------------------------------------------
-- 호스트:                          localhost
-- 서버 버전:                        10.4.12-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 테이블 gmp.batch_job_t 구조 내보내기
DROP TABLE IF EXISTS `batch_job_t`;
CREATE TABLE IF NOT EXISTS `batch_job_t` (
  `BATCH_JOB_SEQ` bigint(20) NOT NULL COMMENT '일련번호',
  `JOB_NAME` varchar(128) DEFAULT NULL COMMENT '잡 이름',
  `JOB_CRON` varchar(32) DEFAULT NULL COMMENT '잡 스케뉼(매월 1일  1시 30분 실행 : 0 30 1 1 * ?)',
  `JOB_TYPE` varchar(1) DEFAULT 'D' COMMENT '잡 종류(S : 정적, D : 동적)',
  `JOB_ON` varchar(1) DEFAULT 'N' COMMENT 'Y : 사용, N : 사용안함',
  `SCHEDULER` varchar(250) DEFAULT NULL COMMENT '스케쥴러(Spring Scheduler, Apach camel quartz)',
  `JOB_DESC` varchar(512) DEFAULT NULL COMMENT '설명',
  `JOB_EXECUTE_DATE` datetime DEFAULT NULL COMMENT '잡 실행일시',
  `REG_ID` varchar(40) DEFAULT NULL COMMENT '등록자 ID',
  `REG_DATE` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일',
  `UPD_ID` varchar(40) DEFAULT NULL COMMENT '수정자 ID',
  `UPD_DATE` datetime NOT NULL COMMENT '수정일',
  PRIMARY KEY (`BATCH_JOB_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='배치 잡 관리';

-- 테이블 데이터 gmp.batch_job_t:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `batch_job_t` DISABLE KEYS */;
/*!40000 ALTER TABLE `batch_job_t` ENABLE KEYS */;

-- 테이블 gmp.crawler_collection_t 구조 내보내기
DROP TABLE IF EXISTS `crawler_collection_t`;
CREATE TABLE IF NOT EXISTS `crawler_collection_t` (
  `CRAWLER_COLLECTION_SEQ` bigint(20) NOT NULL COMMENT '일련번호',
  `DEFAULT_URL` varchar(128) DEFAULT NULL COMMENT '기본도메인',
  `SITE_NAME` varchar(64) DEFAULT NULL COMMENT '사이트이름',
  `PAGE_NAME` varchar(64) DEFAULT NULL COMMENT '수집페이지명',
  `LINK` varchar(512) DEFAULT NULL COMMENT '링크',
  `TITLE` varchar(128) DEFAULT NULL COMMENT '제목',
  `CONTENTS` text DEFAULT NULL COMMENT '내용',
  `REPLY` text DEFAULT NULL COMMENT '답글',
  `IMAGES` varchar(512) DEFAULT NULL COMMENT '이미지',
  `IMAGES_CONTENT` text DEFAULT NULL COMMENT '이미지 내용',
  `ANALYSIS_CONTENT` text DEFAULT NULL COMMENT '분석내용',
  `CATEGORY` varchar(32) DEFAULT NULL COMMENT '카테고리',
  `SOURCE` varchar(64) DEFAULT NULL COMMENT '출처',
  `VIEWS` varchar(8) DEFAULT NULL COMMENT '조회',
  `REG_NO` varchar(16) DEFAULT NULL COMMENT '접수번호',
  `ITEM` varchar(32) DEFAULT NULL COMMENT '품목',
  `SATISFACTION` varchar(8) DEFAULT NULL COMMENT '만족도',
  `KIND` varchar(32) DEFAULT NULL COMMENT '품종',
  `PRODUCT` varchar(32) DEFAULT NULL COMMENT '해당품목',
  `WRITER` varchar(32) DEFAULT NULL COMMENT '작성자',
  `ATTACHED` varchar(128) DEFAULT NULL COMMENT '첨부자료',
  `REG_DATE` varchar(32) DEFAULT NULL COMMENT '등록일',
  `REPLY_DATE` varchar(32) DEFAULT NULL COMMENT '답변일',
  `ANLS_DATE` datetime DEFAULT NULL COMMENT '분석일',
  PRIMARY KEY (`CRAWLER_COLLECTION_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='크롤러의 수집 정보';

-- 테이블 데이터 gmp.crawler_collection_t:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `crawler_collection_t` DISABLE KEYS */;
/*!40000 ALTER TABLE `crawler_collection_t` ENABLE KEYS */;

-- 테이블 gmp.crawler_t 구조 내보내기
DROP TABLE IF EXISTS `crawler_t`;
CREATE TABLE IF NOT EXISTS `crawler_t` (
  `CRAWLER_SEQ` bigint(20) NOT NULL COMMENT '일련번호',
  `CRAWLER_NAME` varchar(32) DEFAULT NULL COMMENT '크롤러이름',
  `SITE_NAME` varchar(64) DEFAULT NULL COMMENT '사이트명',
  `DEFAULT_URL` varchar(128) DEFAULT NULL COMMENT '기본URL',
  `SEARCH_URL` varchar(512) DEFAULT NULL COMMENT '검색URL',
  `COLLECTION` varchar(16) DEFAULT NULL COMMENT '수집대상(수집대상)',
  `PAGE_NAME` varchar(64) DEFAULT NULL COMMENT '페이지명',
  `KEYWORD` varchar(64) DEFAULT NULL COMMENT '기본키워드(, 로 구분)',
  `CONTENT_METHOD` varchar(8) DEFAULT NULL COMMENT '컨덴츠메소드(POST, GET)',
  `CONTENT_TYPE` varchar(32) DEFAULT NULL COMMENT '컨텐츠타입(application/x-www-form-urlencoded; charset=uft-8)',
  `PAGE_EL` varchar(64) DEFAULT NULL COMMENT '페이지요소',
  `LIST_EL` varchar(64) DEFAULT NULL COMMENT '목록요소',
  `BODY_EL` varchar(64) DEFAULT NULL COMMENT '본문요소',
  `TITLE_EL` varchar(64) DEFAULT NULL COMMENT '제목요소',
  `CONTENTS_EL` varchar(64) DEFAULT NULL COMMENT '내용요소',
  `REPLY_EL` varchar(64) DEFAULT NULL COMMENT '답글요소',
  `ITEM_EL` varchar(64) DEFAULT NULL COMMENT '항목요소',
  `KEYWORD_DETAILS` varchar(64) DEFAULT NULL COMMENT '상세키워드(, 로 구분)',
  `FILTER` varchar(64) DEFAULT NULL COMMENT '필터(, 로 구분)',
  `REPLY_YN` varchar(1) DEFAULT NULL COMMENT '답글여부(Y,N)',
  `USE_YN` varchar(1) DEFAULT NULL COMMENT '사용여부(Y,N)',
  `DEL_YN` varchar(1) DEFAULT NULL COMMENT '삭제여부(Y,N)',
  `COLLECT_DATE` datetime DEFAULT NULL COMMENT '최종수집일',
  `REG_ID` varchar(16) DEFAULT NULL COMMENT '등록자',
  `REG_DATE` datetime DEFAULT NULL COMMENT '등록일',
  `UPD_ID` varchar(16) DEFAULT NULL COMMENT '수정자',
  `UPD_DATE` datetime DEFAULT NULL COMMENT '수정일',
  PRIMARY KEY (`CRAWLER_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='크롤러 정보';

-- 테이블 데이터 gmp.crawler_t:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `crawler_t` DISABLE KEYS */;
/*!40000 ALTER TABLE `crawler_t` ENABLE KEYS */;

-- 테이블 gmp.sequence 구조 내보내기
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE IF NOT EXISTS `sequence` (
  `SEQ` varchar(255) NOT NULL,
  `NEXT_VAL` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 gmp.sequence:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `sequence` DISABLE KEYS */;
INSERT INTO `sequence` (`SEQ`, `NEXT_VAL`) VALUES
	('CRAWLER_COLLECTION_T', 18),
	('CRAWLER_T', 5);
/*!40000 ALTER TABLE `sequence` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
