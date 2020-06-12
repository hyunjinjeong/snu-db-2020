def print_all_buildings(db):
    building_sql = 'SELECT id, name, location, capacity FROM building;'
    buildings = db.fetch(building_sql)

    for building in buildings:
        building_id = building['id']
        count_assign_sql = f'SELECT COUNT(*) as count FROM assign WHERE building_id = {building_id};'
        count = db.fetch(count_assign_sql)[0]['count']
        building['assigned'] = count

    print(buildings)


def print_all_performances(db):
    performance_sql = 'SELECT id, name, type, price FROM performance;'
    performances = db.fetch(performance_sql)

    for performance in performances:
        performance_id = performance['id']
        count_book_sql = f'SELECT COUNT(*) as count FROM book WHERE performance_id = {performance_id};'
        count = db.fetch(count_book_sql)[0]['count']
        performance['booked'] = count

    print(performances)


def print_all_audiences(db):
    sql = 'SELECT id, name, gender, age FROM audience;'
    print(db.fetch(sql))


def add_building(db):
    name = input('Building name: ')[:200]
    location = input('Building location: ')[:200]
    capacity = int(input('Building capacity: '))

    if capacity < 1:
        print('Capacity should be more than 0')
        return

    sql = f'INSERT INTO building (name, location, capacity) VALUES ("{name}", "{location}", {capacity});'
    db.execute(sql)
    print('A building is successfully inserted')


def remove_building(db):
    building_id = int(input('Building id: '))
    if buildig_not_exists(db, building_id):
        print(f"Building {building_id} doesn't exist")
        return

    remove_sql = f'DELETE FROM building WHERE id = {building_id};'
    db.execute(remove_sql)
    print('A building is successfully removed')


def buildig_not_exists(db, building_id):
    check_sql = f'SELECT COUNT(*) AS count FROM building WHERE id = {building_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


def add_performance(db):
    name = input('Performance name: ')[:200]
    type = input('Performance type: ')[:200]
    price = int(input('Performance price: '))

    if price < 0:
        print('Price should be 0 or more')
        return

    sql = f'INSERT INTO performance (name, type, price) values ("{name}", "{type}", {price});'
    db.execute(sql)
    print('A performance is successfully inserted')


def remove_performance(db):
    performance_id = int(input('Performance id: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist")
        return

    remove_sql = f'DELETE FROM performance WHERE id = {performance_id};'
    db.execute(remove_sql)
    print('A performance is successfully removed')


def performance_not_exists(db, performance_id):
    check_sql = f'SELECT COUNT(*) AS count FROM performance WHERE id = {performance_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


def add_audience(db):
    name = input('Audience name: ')[:200]
    gender = input('Audience gender: ')[:200]

    if not (gender == 'M' or gender == 'F'):
        print("Gender should be 'M' or 'F'")
        return

    age = int(input('Audience age: '))
    
    if age < 1:
        print('Age should be more than 0')
        return

    sql = f'INSERT INTO audience (name, gender, age) values ("{name}", "{gender}", {age});'
    db.execute(sql)
    print('An audience is successfully inserted')


def remove_audience(db):
    audience_id = int(input('Audience id: '))
    if audience_not_exists(db, audience_id):
        print(f"Audience {audience_id} doesn't exist")
        return

    remove_sql = f'DELETE FROM audience WHERE id = {audience_id};'
    db.execute(remove_sql)
    print('An audience is successfully removed')


def audience_not_exists(db, audience_id):
    check_sql = f'SELECT COUNT(*) AS count FROM audience WHERE id = {audience_id};'
    count = db.fetch(check_sql)[0]['count']
    if count == 0:
        return True
    return False


def assign_performance(db):
    building_id = int(input('Building ID: '))
    if buildig_not_exists(db, building_id):
        print(f"Building {building_id} doesn't exist")
        return

    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist")
        return
    
    if is_performance_assigned(db, performance_id):
        print(f'Performance {performance_id} is already assigned')
        return
    
    insert_sql = f'INSERT INTO assign (building_id, performance_id) values ("{building_id}", "{performance_id}");'
    db.execute(insert_sql)

    print('Successfully assign a performance')


def is_performance_assigned(db, performance_id):
    check_sql = f'SELECT COUNT(*) AS count FROM assign WHERE performance_id = {performance_id};'
    count = db.fetch(check_sql)[0]['count']
    if count > 0:
        return True
    return False


def book_performance(db):
    performance_id = int(input('Performance ID: '))
    if performance_not_exists(db, performance_id):
        print(f"Performance {performance_id} doesn't exist")
        return
    if not is_performance_assigned(db, performance_id):
        print(f"Performance {performance_id} isn't assigned")
        return

    audience_id = int(input('Audience id: '))
    if audience_not_exists(db, audience_id):
        print(f"Audience {audience_id} doesn't exist")
        return

    capacity = get_capacity_by_performance(db, performance_id)
    seat_numbers = list(map(int, input('Seat number: ').replace(" ", "").split(",")))
    if is_seat_numbers_invalid(capacity, seat_numbers):
        print('Seat number out of range')
        return
    if is_seat_numbers_taken(db, performance_id, seat_numbers):
        print('The seat is already taken')
        return
    
    total_ticket_price = 0
    for seat_number in seat_numbers:
        insert_sql = f'INSERT INTO book (performance_id, audience_id, seat_number) VALUES ({performance_id}, {audience_id}, {seat_number});'
        db.execute(insert_sql)
        total_ticket_price += calculate_ticket_price(db, performance_id, audience_id)
    total_ticket_price = get_round(total_ticket_price)
    
    print('Successfully book a performance')
    print(f'Total ticket price is {total_ticket_price:,}')


def get_capacity_by_performance(db, performance_id):
    get_building_id_sql = f'SELECT building_id FROM assign WHERE performance_id = {performance_id};'
    building_id = db.fetch(get_building_id_sql)[0]['building_id']

    get_capacity_sql = f'SELECT capacity FROM building WHERE id = {building_id};'
    capacity = db.fetch(get_capacity_sql)[0]['capacity']
    return capacity


def is_seat_numbers_invalid(capacity, seat_numbers):
    for seat_number in seat_numbers:
        if not 1 <= seat_number <= capacity:
            return True
    return False


def is_seat_numbers_taken(db, performance_id, seat_numbers):
    for seat_number in seat_numbers:
        sql = f'SELECT COUNT(*) as count FROM book WHERE performance_id = {performance_id} AND seat_number = {seat_number};'
        count = db.fetch(sql)[0]['count']
        if count > 0:
            return True
    return False


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


# Python의 round 방식에 따른 문제 때문에 직접 구현.
# 문제 ex) round(4.5) = 4, round(3.5) = 4
def get_round(num):
    if num - int(num) >= 0.5:
        return int(num) + 1
    return int(num)


def print_assigned_performances(db):
    print('print assigned performances')


def print_booked_audiences(db):
    print('print booked audiences')


def print_booking_status_of_performance(db):
    performance_id = int(input('Performance ID: '))
    print('print booking status of performance')


def reset_database(db):
    answer = input('This action is irreversible! Do you really want to reset the database? (y/n): ')
    if answer == 'Y' or answer == 'y':
        db.reset()


map_commands = {
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
