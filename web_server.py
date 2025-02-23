from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/login', methods=['POST'])
def authentication():
    
    data = request.get_json()
    
    username = data.get('username')
    password = data.get('password')
    
    if username == "test" and password == "test":
        return jsonify({"message": "Login Successful!"}), 200
    else:
        return jsonify({"message": "Invalid credentials!"}), 401

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=443, ssl_context=('domain_test.pem', 'domain_test.key'))