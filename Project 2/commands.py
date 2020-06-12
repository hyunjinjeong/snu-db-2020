def print_all_buildings(db):
    sql = '''
        select * from building;
    '''
    print(db.fetch(sql))


def print_all_performances(db):
    sql = '''
        select * from performance;
    '''
    print(db.fetch(sql))


def print_all_audiences(db):
    sql = '''
        select * from audience;
    '''
    print(db.fetch(sql))


def add_building(db):
    name = input('Building name: ')
    location = input('Building location: ')
    capacity = int(input('Building capacity: '))

    if capacity < 1:
        print('Capacity should be more than 0')
        return

    sql = f'insert into building (name, location, capacity) values ("{name}", "{location}", {capacity})'
    db.execute(sql)
    print('A building is successfully inserted')


def remove_building(db):
    building_id = int(input('Building id: '))
    print('A building is successfully removed')


def add_performance(db):
    name = input('Performance name: ')
    type = input('Performance type: ')
    price = int(input('Building capacity: '))
    print('A performance is successfully inserted')


def remove_performance(db):
    performance_id = int(input('Performance id: '))
    print('A performance is successfully removed')


def add_audience(db):
    name = input('Audience name: ')
    gender = input('Audience gender: ')
    age = int(input('Audience age: '))
    print('An audience is successfully inserted')


def remove_audience(db):
    audience_id = int(input('Audience id: '))
    print('An audience is successfully removed')


def assign_performance(db):
    building_id = int(input('Building ID: '))
    performance_id = int(input('Performance ID: '))
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
