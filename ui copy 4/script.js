let stream;
let captureInterval;
let eventLog = []; // store all frames' results until stop

document.getElementById('startButton').addEventListener('click', startSession);
document.getElementById('stopButton').addEventListener('click', stopSession);

async function startSession() {
  const video = document.getElementById('video');
  try {
    // Clear old log and UI
    eventLog = [];
    document.getElementById('eventlog').innerHTML = '';
    document.querySelector('#report p').textContent = 'Session started...';

    // Start webcam
    stream = await navigator.mediaDevices.getUserMedia({ video: true });
    video.srcObject = stream;

    // Start capturing frames
    startCapturingFrames();
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

function startCapturingFrames() {
  // 100 ms = 10 frames/sec. Adjust as needed.
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

    // Convert to Blob
    const dataUrl = canvas.toDataURL('image/jpeg');
    const blob = await (await fetch(dataUrl)).blob();

    const formData = new FormData();
    formData.append('frame', blob);

    const response = await fetch('http://127.0.0.1:5000/analyze', {
      method: 'POST',
      body: formData
    });
    const result = await response.json();

    if (result.status === 0) {
      console.error(result.message);
      document.querySelector('#report p').textContent = result.message;
    } else {
      // For example, data = { datetime, emotion, eye }
      const { datetime, emotion, eye } = result.data;

      // Live result in #report
      document.querySelector('#report p').textContent =
        `Emotion: ${emotion} | Eye: ${eye}`;

      // Append to eventLog
      eventLog.push({ datetime, emotion, eye });

      // Also append a line in #eventlog
      const logLine = document.createElement('p');
      logLine.textContent = `[${datetime}] Emotion: ${emotion}, Eye: ${eye}`;
      document.getElementById('eventlog').appendChild(logLine);
    }
  } catch (error) {
    console.error('Error:', error);
    document.querySelector('#report p').textContent = 'Error fetching data';
  }
}
