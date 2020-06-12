def print_all_buildings(db):
    sql = '''
        SELECT * FROM building;
    '''
    print(db.fetch(sql))


def print_all_performances(db):
    sql = '''
        SELECT * FROM performance;
    '''
    print(db.fetch(sql))


def print_all_audiences(db):
    sql = '''
        SELECT * FROM audience;
    '''
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
    
    check_sql = f'SELECT COUNT(*) AS count FROM assign WHERE performance_id = {performance_id};'
    count = db.fetch(check_sql)[0]['count']
    if count > 0:
        print(f'Performance {performance_id} is already assigned')
        return
    
    insert_sql = f'INSERT INTO assign (building_id, performance_id) values ("{building_id}", "{performance_id}");'
    db.execute(insert_sql)

    print('Successfully assign a performance')


def book_performance(db):
    performance_id = int(input('Performance ID: '))
    audience_id = int(input('Audience id: '))
    seat_numbers = list(map(int, input('Seat number: ')))
    print('Successfully book a performance')


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
