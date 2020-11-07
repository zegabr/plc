file1 = open("input", "r")
file2 = open("user_output", "r")

correct = file1.read()
user_output = file2.read()

def process_output(coisa):
    return sorted(coisa)

if process_output(correct) == process_output(user_output):
    print(1)
    exit()

else:
    pass