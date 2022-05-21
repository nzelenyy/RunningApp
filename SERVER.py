from flask import Flask, redirect, url_for, request
app = Flask(__name__)
import csv
import json

def create_table():
    with open("data.csv", "w") as file:
        writer = csv.writer(file)
        writer.writerow(
            ["time", "distance"]
        )
    

def write_to_table(time, distance):
    data_values = [time, distance]
    with open("data.csv", "a") as file:
        writer = csv.writer(file)
        writer.writerow(
            data_values
        )
        
def find_distance(T):
    values = []
    with open("data.csv", "r") as file:
        reader = csv.reader(file)
        for line in reader:
            if (line[0] == str(T)):
                values.append(float(line[1]))
    return (max(values))


create_table()
write_to_table(4, 10)
write_to_table(4, 7)
write_to_table(4,125)
write_to_table(5,64)
#print(find_distance(4))
#create_table()



#@app.route('/success/<name>/<name1>')
#def success(name, name1):
#   return 'welcome home %s' % (str(find_distance(4)))

@app.route('/write/<T>/<distance>')
def write(T, distance):
    write_to_table(int(T), int(distance))
    return 'inserted %s' % str(T)+' '+str(distance)
@app.route('/findMax/<T>')
def findMax(T):
    return (str(find_distance(T)))
@app.route('/clearTable')
def clearTable():
    create_table()
    return 'all data wiped'

#@app.route('/fail/<name>')a
#def fail(name):
#   return 'welcome home %s' % name

@app.route('/login',methods = ['POST', 'GET'])
def login():
   if request.method == 'POST':
      user = request.form['nm']
      return redirect(url_for('success',name = user))
   else:
      user = request.args.get('nm')
      return redirect(url_for('success',name = user))

if __name__ == '__main__':
   app.run(debug = True)
create_table()
