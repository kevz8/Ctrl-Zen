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


        const result = await response.json();
        if(result.status === 0) {
            console.log(result.message);
            throw new Error(result.message);
        }

        console.log('Login successful');
        // jump to the next page.

    } catch (error) {
        console.error('Some unexpected error');

    }
});
