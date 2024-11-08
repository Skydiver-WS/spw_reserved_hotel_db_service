const express = require('express');
const { exec } = require('child_process');

const app = express();
app.use(express.json());

app.post('/webhook', (req, res) => {
    const payload = req.body;

    if (payload && payload.ref === 'refs/heads/main') {
        exec('./deploy.sh', (error, stdout, stderr) => {
            if (error) {
                console.error(`Error: ${error}`);
                return res.sendStatus(500);
            }
            console.log(`Output: ${stdout}`);
            console.error(`Errors: ${stderr}`);
            res.sendStatus(200);
        });
    } else {
        res.sendStatus(200);
    }
});

app.listen(3000, () => console.log('Listening on port 3000'));
