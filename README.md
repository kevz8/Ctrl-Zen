## Inspiration
_Have you ever banged your head against the keyboard for hours on end with nothing to show for it?_

We’re pretty sure every developer has been there; we certainly have. Ctrl+Zen draws inspiration from the simplicity of keyboard shortcuts and the universal need to stay focused when coding. We wanted to create a tool that promotes focus and productivity for developers, blending efficiency with mindfulness to **help code with maximum efficiency**.

## What it does
Ctrl+Zen is a facial-recognition productivity companion that helps manage your developing workflow with customized data attuned to your emotional state. When agitated, we may go against our better judgment instead of taking a breather, Ctrl+Zen is here to fix that. By analyzing your expressions, our application detects when you are laser-focused or nearing burnout, then offers gentle nudges to take breaks or power through. Over time, it compiles personalized insights so you can code more efficiently and accomplish more in less time. Ctrl-Zen empowers developers to **achieve more in less time**.

## How we built it
In the backend, we developed Ctrl+Zen using Python for image analysis and Java for the database. With OpenCV, DeepFace, and TensorFlow to generate image analysis, we were able to create a comprehensive application that provides meaningful results to users. In the front end, we utilized Figma to create the GUI and JavaScript, HTML, and CSS to implement it. The design ensures the interface is intuitive and distraction-free. Action-driven features were integrated to keep the user experience seamless.

## Challenges we ran into
Balancing functionality with simplicity was a significant challenge. We had to carefully prioritize features to avoid overwhelming users while still delivering value. We also had difficulty connecting data transmission between the frontend and backend; however, after much trial and error, we succeeded with local servers through Flask. Configuring the AI computer vision models also required a lot of trial and error to ensure accurate analyses. 

## Accomplishments that we're proud of
We're proud of creating a tool that bridges productivity and personal insights in a meaningful way. Although we faced many difficulties throughout the process of creating this application, we are proud to overcome such challenges and finalize our project.

## What we learned
We learned how to integrate a Flask server to transmit information between the frontend and backend and also redirect between pages. We also learned how to use OpenCV and DeepFace to detect faces and determine a person’s emotions and eye positions. Analyzing when a person is focused or not also proved to be difficult, resulting in many configurations on our AI models.

## What's next for Ctrl+Zen
We plan to:
- Expand Ctrl+Zen with AI-driven productivity insights and suggestions.
- Explore integrations with popular platforms such as Google.
- Build a community around Ctrl-Zen for shared growth and learning.
