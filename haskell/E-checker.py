file1 = open("input", "r")
file2 = open("user_output", "r")

correct = file1.read()
user_output = file2.read()

def process_output(coisa):
    import ast
    return sorted(ast.literal_eval(coisa))

if process_output(correct) == process_output(user_output):
    print(1)

else:
    pass#bota aqui oq tu quiser