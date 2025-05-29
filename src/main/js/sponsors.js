import '../scss/sponsors.scss'
import { animate } from 'animejs'
import axios from 'axios'
import { csrfHeaderName, csrfToken } from './util/csrf.js'

const deleteButton = document.querySelectorAll('.remove-sponsor-button')

deleteButton.forEach(deleteButton => {
    deleteButton.addEventListener('click', async e => {
        e.preventDefault()
        if (!confirm('Are you sure you want to delete this sponsor?')) {
            return
        }

        // Get the sponsor ID from a data attribute on the button
        const sponsorId = deleteButton.getAttribute('data-sponsor-id')

        if (!sponsorId) {
            alert('Sponsor ID is missing!')
            return
        }

        try {
            const response = await axios.delete(`/api/sponsors/${sponsorId}`, {
                headers: {
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                }
            })

            if (response.status === 204) {
                const cardCol = deleteButton.closest('.col')
                if (cardCol) {
                    cardCol.style.height = `${cardCol.offsetHeight}px`
                    cardCol.style.overflow = 'hidden'

                    animate(cardCol, {
                        opacity: [ 1, 0 ],
                        scale: [ 1, 0.95 ],
                        height: [ cardCol.offsetHeight + 'px', '0px' ],
                        duration: 400,
                        easing: 'easeOutQuad',
                        complete: () => {
                            cardCol.remove()
                        }
                    })
                }
            } else {
                alert('Sponsor not found or could not be deleted.')
            }
        } catch (error) {
            console.error('Error deleting sponsor:', error)
            // Better error handling with Axios
            if (error.response) {
                // Server responded with a status code outside 2xx
                console.error('Response data:', error.response.data)
                console.error('Response status:', error.response.status)
                alert('Server error: ' + (error.response.data.message || 'Failed to delete sponsor.'))
            } else if (error.request) {
                // No response received
                console.error('No response received:', error.request)
                alert('No response from server. Please check your connection.')
            } else {
                // Request setup error
                console.error('Request error:', error.message)
                alert('Request error: ' + error.message)
            }
        }
    })
})
