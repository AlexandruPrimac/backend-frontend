import '../scss/races.scss'

import { csrfHeaderName, csrfToken } from './util/csrf.js'

document.addEventListener('DOMContentLoaded', () => {
    const deleteButton = document.getElementById('delete-button')
    const editButton = document.getElementById('edit-button')
    const saveButton = document.getElementById('save-button')
    const cancelButton = document.getElementById('cancel-button')
    const inputs = document.querySelectorAll('input')

    let originalValues = {} // Store original values for cancel
    const raceId = deleteButton.getAttribute('data-race-id')

    // Delete race
    deleteButton.addEventListener('click', async () => {
        if (!confirm('Are you sure you want to delete this race?')) {
            return
        }

        if (!raceId) {
            alert('Race ID is missing!')
            return
        }

        try {
            const response = await fetch(`/api/races/${raceId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                }
            })

            if (response.status === 204) {
                alert('Race deleted successfully!')
                location.href = '/races' // Redirect to the races list page after deletion
            } else {
                alert('Race not found or could not be deleted.')
            }
        } catch (error) {
            console.error('Error deleting race:', error)
            alert('Something went wrong. Please try again.')
        }
    })

    // Enable editing mode
    editButton.addEventListener('click', () => {
        // Save original values
        inputs.forEach(input => originalValues[input.id] = input.value)

        // Enable inputs
        inputs.forEach(input => input.removeAttribute('disabled'))

        // Show Save & Cancel, Hide Edit
        editButton.style.display = 'none'
        saveButton.style.display = 'inline-block'
        cancelButton.style.display = 'inline-block'
    })

    // Cancel editing (restore original values)
    cancelButton.addEventListener('click', () => {
        inputs.forEach(input => input.value = originalValues[input.id]) // Restore values
        inputs.forEach(input => input.setAttribute('disabled', 'true')) // Disable inputs

        // Show Edit, Hide Save & Cancel
        editButton.style.display = 'inline-block'
        saveButton.style.display = 'none'
        cancelButton.style.display = 'none'
    })

    // Save updates via PATCH
    saveButton.addEventListener('click', async () => {
        if (!raceId) {
            alert('Race ID is missing!')
            return
        }

        const updatedRace = {
            name: document.getElementById('race-name').value,
            date: document.getElementById('race-date').value,
            track: document.getElementById('race-track').value,
            location: document.getElementById('race-location').value,
            distance: parseFloat(document.getElementById('race-distance').value)
        }

        try {
            const response = await fetch(`/api/races/${raceId}`, {
                method: 'PATCH',
                headers: {
                    'Accept': 'application/json',
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedRace)
            })

            if (response.ok) {
                alert('Race updated successfully!')
                inputs.forEach(input => input.setAttribute('disabled', 'true')) // Disable inputs
                editButton.style.display = 'inline-block'
                saveButton.style.display = 'none'
                cancelButton.style.display = 'none'
            } else {
                const errorMsg = await response.text()
                alert('Failed to update race: ' + errorMsg)
            }
        } catch (error) {
            console.error('Error updating race:', error)
            alert('Something went wrong. Please try again.')
        }
    })
})
