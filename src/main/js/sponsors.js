import '../scss/sponsors.scss'

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
            const response = await fetch(`/api/sponsors/${sponsorId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeaderName]: csrfToken,
                    'Content-Type': 'application/json'
                }
            })

            if (response.status === 204) {
                const cardCol = deleteButton.closest('.col')
                if (cardCol) {
                    // Animate fade-out + shrink effect
                    cardCol.animate(
                        [
                            { opacity: 1, transform: 'scale(1)', height: `${cardCol.offsetHeight}px` },
                            { opacity: 0, transform: 'scale(0.95)', height: '0px' }
                        ],
                        {
                            duration: 400,
                            easing: 'ease-out',
                            fill: 'forwards'
                        }
                    ).onfinish = () => {
                        cardCol.remove()
                    }
                }
            } else {
                alert('Sponsor not found or could not be deleted.')
            }
        } catch (error) {
            console.error('Error deleting sponsor:', error)
            alert('Something went wrong. Please try again.')
        }
    })
})
