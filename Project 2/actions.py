# 1번 명령어 - 모든 building 출력
def print_all_buildings(db):
    building_sql = 'SELECT id, name, location, capacity FROM building;'
    buildings = db.fetch(building_sql)

    # 해당하는 building에 공연이 할당된 갯수 구하기
    for building in buildings:
        building_id = building['id']
        count_assign_sql = f'SELECT COUNT(*) as count FROM assign WHERE building_id = {building_id};'
        count = db.fetch(count_assign_sql)[0]['count']
        building['assigned'] = count

    print(format_results('building', buildings))


# 2번 명령어 - 모든 performance 출력
def print_all_performances(db):
    performance_sql = 'SELECT id, name, type, price FROM performance;'
    performances = db.fetch(performance_sql)

    # 해당 performance가 예매된 횟수 구하기
    for performance in performances:
        performance_id = performance['id']
        count_book_sql = f'SELECT COUNT(*) as count FROM book WHERE performance_id = {performance_id};'
        count = db.fetch(count_book_sql)[0]['count']
        performance['booked'] = count

    print(format_results('performance', performances))


# 3번 명령어 - 모든 audience 출력
def print_all_audiences(db):
    sql = 'SELECT id, name, gender, age FROM audience;'
    audiences = db.fetch(sql)
    print(format_results('audience', audiences))


# 4번 명령어 - building 추가
def add_building(db):
    name = input('Building name: ')[:200]
    location = input('Building location: ')[:200]
    capacity = int(input('Building capacity: '))

    if capacity < 1:
        print('Capacity should be more than 0\n')
        return

    sql = f'INSERT INTO building (name, location, capacity) VALUES ("{name}", "{location}", {capacity});'
    db.execute(sql)
    print('A building is successfully inserted\n')


# 5번 명령어 - id를 입력받아 해당 building 삭제
def remove_building(db):
    building_id = int(input('Building id: '))
    if buildig_not_exists(db, building_id):
        print(f"Building {building_id} doesn't exist\n")
        return

    remove_sql = f'DELETE FROM building WHERE id = {building_id};'
    db.execute(remove_sql)
    print('A building is successfully removed\n')


# 주어진 id의 building이 있는지 확인
def buildig_not_exists(db, building_id):
    check_sql = f'SELECT COUNT(*) AS count FROM building WHERE id = {building_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


# 6번 명령어 - performance 추가
def add_performance(db):
    name = input('Performance name: ')[:200]
    type = input('Performance type: ')[:200]
    price = int(input('Performance price: '))

    if price < 0:
        print('Price should be 0 or more\n')
        return

    sql = f'INSERT INTO performance (name, type, price) values ("{name}", "{type}", {price});'
    db.execute(sql)
    print('A performance is successfully inserted\n')


# 7번 명령어 - id를 입력받아 해당 performance 삭제
def remove_performance(db):
    performance_id = int(input('Performance id: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist\n")
        return

    remove_sql = f'DELETE FROM performance WHERE id = {performance_id};'
    db.execute(remove_sql)
    print('A performance is successfully removed\n')


# 주어진 id의 performance가 없는지 확인
def performance_not_exists(db, performance_id):
    check_sql = f'SELECT COUNT(*) AS count FROM performance WHERE id = {performance_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


# 8번 명령어 - audience 추가
def add_audience(db):
    name = input('Audience name: ')[:200]
    gender = input('Audience gender: ')[:200]

    if not (gender == 'M' or gender == 'F'):
        print("Gender should be 'M' or 'F'\n")
        return

    age = int(input('Audience age: '))
    
    if age < 1:
        print('Age should be more than 0\n')
        return

    sql = f'INSERT INTO audience (name, gender, age) values ("{name}", "{gender}", {age});'
    db.execute(sql)
    print('An audience is successfully inserted\n')


# 9번 명령어 - id를 입력받아 해당 audience 삭제
def remove_audience(db):
    audience_id = int(input('Audience id: '))
    if audience_not_exists(db, audience_id):
        print(f"Audience {audience_id} doesn't exist\n")
        return

    remove_sql = f'DELETE FROM audience WHERE id = {audience_id};'
    db.execute(remove_sql)
    print('An audience is successfully removed\n')


# 주어진 id의 audience가 존재하는지 확인
def audience_not_exists(db, audience_id):
    check_sql = f'SELECT COUNT(*) AS count FROM audience WHERE id = {audience_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


# 10번 명령어 - performance를 building에 할당
def assign_performance(db):
    building_id = int(input('Building ID: '))
    if buildig_not_exists(db, building_id):
        print(f"Building {building_id} doesn't exist\n")
        return

    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist\n")
        return
    
    if is_performance_assigned(db, performance_id):
        print(f'Performance {performance_id} is already assigned\n')
        return
    
    insert_sql = f'INSERT INTO assign (building_id, performance_id) values ("{building_id}", "{performance_id}");'
    db.execute(insert_sql)

    print('Successfully assign a performance\n')


# 주어진 id의 performance가 할당되었는지 검사
def is_performance_assigned(db, performance_id):
    check_sql = f'SELECT COUNT(*) AS count FROM assign WHERE performance_id = {performance_id};'
    count = db.fetch(check_sql)[0]['count']
    if count > 0:
        return True
    return False


# 11번 명령어 - audience와 seat_number를 performance에 할당
def book_performance(db):
    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist\n")
        return
    if not is_performance_assigned(db, performance_id):
        print(f"Performance {performance_id} isn't assigned\n")
        return

    audience_id = int(input('Audience id: '))
    if audience_not_exists(db, audience_id):
        print(f"Audience {audience_id} doesn't exist\n")
        return

    # performance가 할당된 building의 정원을 구한다
    capacity = get_capacity_by_performance(db, performance_id)
    seat_numbers = list(map(int, input('Seat number: ').replace(" ", "").split(",")))
    if are_seat_numbers_invalid(capacity, seat_numbers):
        print('Seat number out of range\n')
        return
    if are_seat_numbers_taken(db, performance_id, seat_numbers):
        print('The seat is already taken\n')
        return
    
    total_ticket_price = 0
    for seat_number in seat_numbers:
        insert_sql = f'INSERT INTO book (performance_id, audience_id, seat_number) VALUES ({performance_id}, {audience_id}, {seat_number});'
        db.execute(insert_sql)
        total_ticket_price += calculate_ticket_price(db, performance_id, audience_id)
    # 결과 반올림
    total_ticket_price = get_round(total_ticket_price)
    
    print('Successfully book a performance')
    print(f'Total ticket price is {total_ticket_price:,}\n')


# 주어진 id의 performance가 할당된 building의 정원을 구한다
def get_capacity_by_performance(db, performance_id):
    get_building_id_sql = f'SELECT building_id FROM assign WHERE performance_id = {performance_id};'
    building_id = db.fetch(get_building_id_sql)[0]['building_id']

    get_capacity_sql = f'SELECT capacity FROM building WHERE id = {building_id};'
    capacity = db.fetch(get_capacity_sql)[0]['capacity']
    return capacity


# 범위를 벗어난 seat_number가 있는지 확인
def are_seat_numbers_invalid(capacity, seat_numbers):
    for seat_number in seat_numbers:
        if not 1 <= seat_number <= capacity:
            return True
    return False


# 이미 예약되어 있는 seat_number가 있는지 확인
def are_seat_numbers_taken(db, performance_id, seat_numbers):
    for seat_number in seat_numbers:
        sql = f'SELECT COUNT(*) as count FROM book WHERE performance_id = {performance_id} AND seat_number = {seat_number};'
        count = db.fetch(sql)[0]['count']
        if count > 0:
            return True
    return False


# 나이에 따른 ticket price 구하기
def calculate_ticket_price(db, performance_id, audience_id):
    price_sql = f'SELECT price FROM performance WHERE id = {performance_id};'
    age_sql = f'SELECT age FROM audience WHERE id = {audience_id};'
    
    original_price = db.fetch(price_sql)[0]['price']
    age = db.fetch(age_sql)[0]['age']

    if 1 <= age <= 7:
        return 0
    if 8 <= age <= 12:
        return original_price * 0.5
    if 13 <= age <= 18:
        return original_price * 0.2
    return original_price


# Python의 round 방식에 따른 문제 때문에 직접 반올림 구현.
# 문제 예시: round(4.5) = 4, round(3.5) = 4
def get_round(num):
    if num - int(num) >= 0.5:
        return int(num) + 1
    return int(num)


# 12번 명령어 - 주어진 id의 building에 할당된 공연 리스트 출력
def print_assigned_performances(db):
    building_id = int(input('Building ID: '))
    if buildig_not_exists(db, building_id):
        print(f"Building {building_id} doesn't exist\n")
        return
    
    performance_sql = f'''
        SELECT id, name, type, price FROM performance p LEFT JOIN assign a ON (p.id = a.performance_id)
            WHERE a.building_id = {building_id};
    '''
    performances = db.fetch(performance_sql)

    for performance in performances:
        performance_id = performance['id']
        count_book_sql = f'SELECT COUNT(*) as count FROM book WHERE performance_id = {performance_id};'
        count = db.fetch(count_book_sql)[0]['count']
        performance['booked'] = count

    print(format_results('performance', performances))


# 13번 명령어 - 주어진 id의 performance에 예매한 audience 리스트 출력
def print_booked_audiences(db):
    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist\n")
        return

    audience_sql = f'''
        SELECT DISTINCT id, name, gender, age FROM audience a LEFT JOIN book b ON (a.id = b.audience_id)
            WHERE b.performance_id = {performance_id};
    '''
    audiences = db.fetch(audience_sql)
    print(format_results('audience', audiences))


# 14번 명령어 - 주어진 id의 performance의 seat_number 별 예매 현황 출력
def print_booking_status_of_performance(db):
    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist\n")
        return
    if not is_performance_assigned(db, performance_id):
        print(f"Performance {performance_id} isn't assigned\n")
        return
    
    capacity = get_capacity_by_performance(db, performance_id)
    seat_numbers = [i for i in range(1, capacity+1)]
    
    status = []
    for seat_number in seat_numbers:
        temp_status = {'seat_number': seat_number}
        sql = f'SELECT audience_id FROM book WHERE performance_id = {performance_id} AND seat_number = {seat_number};'
        audience = db.fetch(sql)
        if not audience:
            temp_status['audience_id'] = ''
        else:    
            audience_id = audience[0]['audience_id']
            temp_status['audience_id'] = audience_id
        status.append(temp_status)
    
    print(format_results('status', status))


# 16번 명령어 - 데이터베이스의 모든 table을 drop하고 다시 생성. 초기화.
def reset_database(db):
    answer = input('This action is irreversible! Do you really want to reset the database? (y/n): ')
    if answer == 'Y' or answer == 'y':
        db.reset()


# SELECT 문의 결과를 주어진 형식에 맞게 변형
def format_results(type, results):
    line = '--------------------------------------------------------------------------------\n'
    res = line

    if type == 'building':
        headers = ['id', 'name', 'location', 'capacity', 'assigned']
        formats = [8, 32, 16, 16, 8]
    elif type == 'performance':
        headers = ['id', 'name', 'type', 'price', 'booked']
        formats = [8, 32, 16, 16, 8]
    elif type == 'audience':
        headers = ['id', 'name', 'gender', 'age']
        formats = [8, 40, 16, 16]
    elif type == 'status':
        headers = ['seat_number', 'audience_id']
        formats = [40, 40]

    for i in range(len(headers)):
        res += f'{headers[i]:<{formats[i]}}'
    res += '\n'

    res += line

    for row in results:
        temp_result = ''
        for i in range(len(headers)):
            temp_result += f'{row[headers[i]]:<{formats[i]}}'
        res += temp_result
        res += '\n'

    if not results:
        res += '\n'
    
    res += line
    return res


# action 번호에 해당하는 함수 맵핑
map_actions = {
    1: print_all_buildings,
    2: print_all_performances,
    3: print_all_audiences,
    4: add_building,
    5: remove_building,
    6: add_performance,
    7: remove_performance,
    8: add_audience,
    9: remove_audience,
    10: assign_performance,
    11: book_performance,
    12: print_assigned_performances,
    13: print_booked_audiences,
    14: print_booking_status_of_performance,
    16: reset_database
}
