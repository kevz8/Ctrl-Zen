let stream;
let captureInterval;

document.getElementById('startButton').addEventListener('click', async () => {
    const video = document.getElementById('video');
    try {
        stream = await navigator.mediaDevices.getUserMedia({ video: true });
        video.srcObject = stream;
        startCapturingFrames();
    } catch (err) {
        console.error('Error accessing webcam: ', err);
    }
});

document.getElementById('stopButton').addEventListener('click', () => {
    if (stream) {
        const tracks = stream.getTracks();
        tracks.forEach(track => track.stop());
        document.getElementById('video').srcObject = null;
        clearInterval(captureInterval);
    }
});

function startCapturingFrames() {
    captureInterval = setInterval(analyzeFrame, 2);
}


async function analyzeFrame() {
    try {
        const video = document.getElementById('video');
        const canvas = document.createElement('canvas');
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        const context = canvas.getContext('2d');
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
        const dataUrl = canvas.toDataURL('image/jpeg');
        const blob = await (await fetch(dataUrl)).blob();

        const formData = new FormData();
        formData.append('frame', blob);

        const response = await fetch('http://127.0.0.1:5000/analyze', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();
        const emotion = result.data;
        const status = result.status;
        const message = result.message;
        if(status == 0) {
            console.log(message);
        } else {
            document.querySelector('.report p').textContent = emotion;
        }
        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('.report p').textContent = 'Error fetching data';
    }
}