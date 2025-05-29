import '../scss/addRace.scss'
import axios from 'axios'
import Joi from 'joi'

import { csrfHeaderName, csrfToken } from './util/csrf.js'

const raceSchema = Joi.object({
    name: Joi.string().min(2).required().label('Race name'),
    date: Joi.date().min(1 - 1 - 1890).required().label('Date'),
    track: Joi.string().min(2).required().label('Race Track'),
    location: Joi.string().min(2).required().label('Race Location'),
    distance: Joi.number().min(0.5).label('Race Distance')
})

const form = document.getElementById('add-race-form')

form.addEventListener('submit', async e => {
    e.preventDefault()

    const name = document.querySelector('#name').value
    const date = document.querySelector('#date').value
    const track = document.querySelector('#track').value
    const location = document.querySelector('#location').value
    const distance = document.querySelector('#distance').value

    const formData = { name, date, track, location, distance }
    const { error } = raceSchema.validate(formData, { abortEarly: false })
    const errorMessage = document.querySelector('#error-messages')

    if (error) {
        errorMessage.innerHTML = `
            <ul>
                ${error.details.map(err => `<li>${err.message}</li>`).join('')}
            </ul>
        `
        return
    } else {
        errorMessage.innerHTML = ''
    }

    try {
        const jsonBody = { name, date, track, location, distance }
        console.log('Sending data:', jsonBody)

        const response = await axios.post(
            'http://localhost:8080/api/races',
            jsonBody,
            {
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeaderName]: csrfToken,
                    'Accept': 'application/json'
                }
            }
        )

        console.log('Response status:', response.status)

        if (response.status === 201) {
            const race = await response.data
            alert(`Congrats, your race got created. It has ID #${race.id}`)
            window.location = `/race/${race.id}`
        } else {
            const errorMsg = await response.data
            console.error('Server error:', errorMsg)
            alert('Something went wrong: ' + errorMsg)
        }
    } catch (error) {
        console.error('Error during fetch:', error)
        if (error.response) {
            // The request was made and the server responded with a status code
            console.error('Response data:', error.response.data)
            console.error('Response status:', error.response.status)
            alert('Server error: ' + (error.response.data.message || JSON.stringify(error.response.data)))
        } else if (error.request) {
            // The request was made but no response was received
            console.error('No response received:', error.request)
            alert('No response from server. Is it running?')
        } else {
            // Something happened in setting up the request
            console.error('Request setup error:', error.message)
            alert('Request error: ' + error.message)
        }
    }
})
