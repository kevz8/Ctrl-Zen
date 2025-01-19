document.getElementById('signInButton').addEventListener('click', async function() {
    const email = document.querySelector('.input .label').value;
    const password = document.querySelector('.input-2 .placeholder').value;

    const data = {
        username: email,
        password: password
    };

    try {
        const response = await fetch('http://your-api-endpoint.com/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const result = await response.json();
        console.log('Success:', result);
        // Handle success (e.g., redirect to another page)
    } catch (error) {
        console.error('Error:', error);
        // Handle error (e.g., show error message to the user)
    }
});