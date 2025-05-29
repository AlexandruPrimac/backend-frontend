import '../scss/addCar.scss'
import axios from 'axios'
import Joi from 'joi'

import { csrfHeaderName, csrfToken } from './util/csrf.js'

const carSchema = Joi.object({
    brand: Joi.string().min(2).required().label('Brand'),
    model: Joi.string().min(1).required().label('Model'),
    engine: Joi.number().greater(0).required().label('Engine Size'),
    horsePower: Joi.number().min(50).required().label('Horsepower'),
    year: Joi.number().integer().min(1880).max(new Date().getFullYear()).required().label('Year'),
    category: Joi.string().valid('F1', 'RALLY', 'SPORTS', 'DRAG').required().label('Category')
})

const form = document.querySelector('#add-car-form')

form.addEventListener('submit', async e => {
    e.preventDefault()

    const brand = document.querySelector('#brand').value
    const model = document.querySelector('#model').value
    const engine = document.querySelector('#engine').value
    const horsePower = Number(document.querySelector('#horsepower').value)
    const year = document.querySelector('#year').value
    const category = document.querySelector('#category').value

    // 2. Validate before submission
    const formData = { brand, model, engine, horsePower, year, category }
    const { error } = carSchema.validate(formData, { abortEarly: false })
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
        const jsonBody = { brand, model, engine, horsePower, year, category }
        console.log('Sending data:', jsonBody)

        const response = await axios.post(
            'http://localhost:8080/api/cars',
            jsonBody,
            {
                headers: {
                    'Accept': 'application/json',
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                }
            }
        )

        console.log('Response status:', response.status)

        if (response.status === 201) {
            const car = await response.data
            alert(`Congrats, your car got created. It has ID #${car.id}`)
            window.location = `/car/${car.id}`
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
