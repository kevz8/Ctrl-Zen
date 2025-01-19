// Attach event listener to #signInButton for login functionality
document.getElementById('signInButton').addEventListener('click', async function() {
    // Grab the input values from the form
    const email = document.querySelector('.input input.placeholder-2').value;
    const password = document.querySelector('.input-2 input.placeholder').value;

    console.log("Hi, trying to login with:", email, password);
    if(email === "nwhack" && password === "12345678") {
        console.log("success");
        // window.open("ui copy 7/index.html", "_blank");
        //window.location.href = "http://localhost:63342/Ctrl-Zen/ui%20copy%207/index.html?_ijt=3me3j23mi8obsektrqlfr8ijji";
        window.location.href = "file:///Users/henilagrawal/Ctrl-Zen-1/frontend/index.html";
    } else {
        console.log("fail");
    }

    // // Adjust these fields based on what your backend actually expects
    // const data = {
    //     username: email, // or "email: email" if your backend expects 'email' instead of 'username'
    //     password: password
    // };
    //
    // try {
    //     // Send credentials to your backend
    //     const response = await fetch('http://localhost:8080/api/login', {
    //         method: 'POST',
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         body: JSON.stringify(data)
    //     });
    //
    //     // Handle potential HTTP errors
    //     if (!response.ok) {
    //         throw new Error(`HTTP error! Status: ${response.status}`);
    //     }
    //
    //     // Parse the JSON response
    //     const result = await response.json();
    //
    //     // Example handling: if status === 0, login failed
    //     if (result.status === 0) {
    //         console.log(result.message);
    //         throw new Error(result.message);
    //     }
    //
    //     // Otherwise, assume login is successful
    //     console.log('Login successful');
    //
    //     // Redirect to the next page on success
    //     window.location.href = "/dashboard.html";
    //
    // } catch (error) {
    //     // Log any errors (network issues, bad credentials, etc.)
    //     console.error('Login failed:', error.message || error);
// }
});
