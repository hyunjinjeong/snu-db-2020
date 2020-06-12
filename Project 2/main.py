from database import load_db
from commands import map_commands


# 초기 메시지 생성
def get_initial_message():
    message = ''
    command_message = {
        1: 'print all buildings',
        2: 'print all performances',
        3: 'print all audiences',
        4: 'insert a new building',
        5: 'remove a buildling',
        6: 'insert a new performance',
        7: 'remove a performance',
        8: 'insert a new audience',
        9: 'remove an audience',
        10: 'assign a performance to a building',
        11: 'book a performance',
        12: 'print all performances which assgned at a building',
        13: 'print all audiences who booked for a performance',
        14: 'print ticket booking status of a performance',
        15: 'exit',
        16: 'reset database'
    }

    message += '============================================================\n'
    for i in range(1, 17):
        temp_message = f'{i}. {command_message[i]}\n'
        message += temp_message
    message += '============================================================'
    return message


# 유저가 입력한 번호에 따라 명령 실행
def execute_command(db, cmd):
    if not 1 <= cmd <= 16:
        print('Invalid action')
        return
    map_commands[cmd](db)


# 프로그램 종료
def exit_db(db):
    del db
    print('Bye!')


if __name__ == '__main__':
    db = load_db()
    print(get_initial_message())
    
    while True:
        command = input('Select your action: ')    
        if not command.isdigit():
            print('Invalid action')
            continue

        command = int(command)
        if command == 15:
            exit_db(db)
            break
        execute_command(db, command)
