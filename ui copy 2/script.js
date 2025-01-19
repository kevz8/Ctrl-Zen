let stream;
let captureInterval;

// Attach event listeners to the "start session" and "end session" buttons
document.getElementById('startButton').addEventListener('click', startSession);
document.getElementById('stopButton').addEventListener('click', stopSession);

async function startSession() {
  const video = document.getElementById('video');
  try {
    stream = await navigator.mediaDevices.getUserMedia({ video: true });
    video.srcObject = stream;
    startCapturingFrames();
    document.querySelector('#report p').textContent = 'Session started...';
  } catch (err) {
    console.error('Error accessing webcam:', err);
    document.querySelector('#report p').textContent = 'Error accessing webcam';
  }
}

function stopSession() {
  if (stream) {
    const tracks = stream.getTracks();
    tracks.forEach(track => track.stop());
    document.getElementById('video').srcObject = null;
    clearInterval(captureInterval);
    document.querySelector('#report p').textContent = 'Session ended.';
  }
}

// Starts capturing frames every X ms
function startCapturingFrames() {
  // Adjust interval as needed. 100 ms = 10 frames/sec
  captureInterval = setInterval(analyzeFrame, 100);
}

async function analyzeFrame() {
  try {
    const video = document.getElementById('video');
    if (!video || video.videoWidth === 0 || video.videoHeight === 0) return;

    // Draw current frame onto a canvas
    const canvas = document.createElement('canvas');
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    const context = canvas.getContext('2d');
    context.drawImage(video, 0, 0, canvas.width, canvas.height);

    // Convert canvas data to a Blob
    const dataUrl = canvas.toDataURL('image/jpeg');
    const blob = await (await fetch(dataUrl)).blob();

    // Send the frame to server
    const formData = new FormData();
    formData.append('frame', blob);

    const response = await fetch('http://127.0.0.1:5000/analyze', {
      method: 'POST',
      body: formData
    });

    // Handle server response
    const result = await response.json();
    if (result.status === 0) {
      console.error(result.message);
      document.querySelector('#report p').textContent = result.message;
    } else {
      // e.g., { datetime, emotion, eye }
      const { emotion, eye } = result.data;
      document.querySelector('#report p').textContent = `Emotion: ${emotion} | Eye: ${eye}`;
    }
  } catch (error) {
    console.error('Error:', error);
    document.querySelector('#report p').textContent = 'Error fetching data';
  }
}
