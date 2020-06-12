import pymysql

def load_db():
    return Database.instance()

# DB는 프로그램에서 하나의 인스턴스만 존재하면 되므로 Singleton 패턴 사용
class Database:
    _instance = None

    @classmethod
    def _get_instance(cls):
        return cls._instance
    
    @classmethod
    def instance(cls, *args, **kwargs):
        cls._instance = cls(*args, **kwargs)
        cls.instance = cls._get_instance
        return cls._instance

    def __init__(self):
        self._connection = pymysql.connect(
            host='astronaut.snu.ac.kr',
            port=3306,
            user='20DB_2013_11431',
            password='20DB_2013_11431',
            db='20DB_2013_11431',
            charset='utf8',
            cursorclass=pymysql.cursors.DictCursor)
        self._initialize_database()
    
    def __del__(self):
        self.connection.close()
    
    @property
    def connection(self):
        return self._connection
    
    def _initialize_database(self):
        with self.connection.cursor() as cursor:
            # TABLE이 없으면 생성.
            # Warning을 일시적으로 무시 후 다시 복구.
            sqls = ['SET sql_notes = 0;'
            ,'''
                CREATE TABLE IF NOT EXISTS building (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    location VARCHAR(200) NOT NULL,
                    capacity INT NOT NULL
                );
            ''', '''
                CREATE TABLE IF NOT EXISTS performance (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    type VARCHAR(200) NOT NULL,
                    price INT NOT NULL
                );
            ''', '''
                CREATE TABLE IF NOT EXISTS audience (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    gender VARCHAR(1) NOT NULL,
                    age INT NOT NULL
                );
            ''', '''
                CREATE TABLE IF NOT EXISTS assign (
                    performance_id INT PRIMARY KEY,
                    building_id INT NOT NULL,
                    CONSTRAINT `fk_assign_building`
                        FOREIGN KEY (building_id) REFERENCES building (id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    CONSTRAINT `fk_assign_performance`
                        FOREIGN KEY (performance_id) REFERENCES performance (id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            ''', '''
                CREATE TABLE IF NOT EXISTS book (
                    performance_id INT NOT NULL,
                    audience_id INT NOT NULL,
                    seat_number INT NOT NULL,
                    PRIMARY KEY (performance_id, seat_number),
                    CONSTRAINT `fk_book_performance`
                        FOREIGN KEY (performance_id) REFERENCES assign (performance_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    CONSTRAINT `fk_book_audience`
                        FOREIGN KEY (audience_id) REFERENCES audience (id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            ''', 'SET sql_notes = 1;']
            for sql in sqls:
                cursor.execute(sql)
    
    # reset 시 테이블의 모든 데이터 삭제
    def reset(self):
        with self.connection.cursor() as cursor:
            sql = 'DROP TABLE building, performance, audience;'
            cursor.execute(sql)
            self._initialize_database()
            self.connection.commit()

    def fetch(self, sql):
        with self.connection.cursor() as cursor:
            cursor.execute(sql)
            result = cursor.fetchall()
            return result
    
    def execute(self, sql):
        with self.connection.cursor() as cursor:
            cursor.execute(sql)
            self.connection.commit();